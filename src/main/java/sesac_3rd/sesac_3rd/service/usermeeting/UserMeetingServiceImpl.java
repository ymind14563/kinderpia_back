package sesac_3rd.sesac_3rd.service.usermeeting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.dto.usermeeting.UserMeetingJoinDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.entity.UserMeeting;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.mapper.usermeeting.UserMeetingMapper;
import sesac_3rd.sesac_3rd.repository.MeetingRepository;
import sesac_3rd.sesac_3rd.repository.UserMeetingRepository;
import sesac_3rd.sesac_3rd.service.meeting.MeetingService;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserMeetingServiceImpl implements UserMeetingService {
    @Autowired
    UserMeetingRepository userMeetingRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MeetingService meetingService;

    // 모임 가입
    @Override
    public void joinMeeting(Long userId, Long meetingId, UserMeetingJoinDTO userMeetingJoinDTO) {
        userMeetingJoinDTO.setUserId(userId); // userId 를 DTO 에 설정
        userMeetingJoinDTO.setMeetingId(meetingId); // meetingId를 DTO 에 설정

        // 이미 참가한 사용자 여부 확인
        if (userMeetingRepository.existsByUser_UserIdAndMeeting_MeetingId(userId, meetingId)) {
            throw new CustomException(ExceptionStatus.MEETING_ALREADY_JOINED);
        }

        // Meeting 엔티티 조회
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));

        // 총 인원을 초과하는지 확인
        if (meeting.getCapacity() >= meeting.getTotalCapacity()) {
            throw new CustomException(ExceptionStatus.MEETING_CAPACITY_FULL);
        }

        // 인증여부 조회
        MeetingDetailDTO meetingDetailDTO = meetingService.getDetailMeeting(meetingId);

        // UserMeeting 엔티티 생성
        UserMeeting userMeeting = UserMeetingMapper.toUserMeetingJoinEntity(userMeetingJoinDTO);
        userMeeting.setMeeting(meeting); // Meeting 객체 설정

        // 인증 여부에 따라 수락 상태 설정
        if (meetingDetailDTO.isAuthType()) {
            userMeeting.setIsAccepted(null); // 인증 대기 상태로 설정
        } else {
            userMeeting.setIsAccepted(true); // 자동 수락
            // 자동 수락된 경우에만 인원 수 추가
            int updatedCapacity = meeting.getCapacity() + userMeetingJoinDTO.getCapacity();
            meeting.setCapacity(updatedCapacity); // 새로운 capacity 설정
            meetingRepository.save(meeting); // 변경된 Meeting 저장
        }

        // UserMeeting 엔티티 저장
        userMeetingRepository.save(userMeeting);

        log.info("모임 참가 성공: 참가한 userId {}", userId);
    }

    // 모임 탈퇴
    public Boolean exitMeeting(Long userId, Long meetingId) {
        // 사용자가 모임에 참가중인지 확인
        boolean isUserJoined = userMeetingRepository.existsByUser_UserIdAndMeeting_MeetingId(userId, meetingId);
        if (!isUserJoined) {
            throw new CustomException(ExceptionStatus.MEETING_NOT_JOINED);
        }
        // UserMeeting entity 가져오기
        UserMeeting userMeeting = userMeetingRepository.findByUser_UserIdAndMeeting_MeetingId(userId, meetingId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));

        // 모임 entity 가져오기
        Meeting meeting = userMeeting.getMeeting();

        // 수용 인원(capacity) 업데이트
        int updatedCapacity = meeting.getCapacity() - 1;
        meeting.setCapacity(Math.max(0, updatedCapacity)); // 음수 방지
        meetingRepository.save(meeting);

        // UserMeeting entity 삭제
        userMeetingRepository.delete(userMeeting);

        log.info("모임 탈퇴 성공: 탈퇴한 userId {}", userId);

        return true;
    }

    // 모임 수락
    public void isAccepted(Long userId, Long meetingId, Long joinUserId) {
        // 특정 모임과 사용자에 대한 UserMeeting entity 찾기
        UserMeeting userMeeting = userMeetingRepository.findByUser_UserIdAndMeeting_MeetingId(userId, meetingId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));

        // Meeting entity 가져오기
        Meeting meeting = userMeeting.getMeeting();

        // 모임장 검증: 현재 사용자가 모임의 작성자인지 확인
        if (!meeting.getUser().getUserId().equals(userId)) {
            throw new CustomException(ExceptionStatus.USER_NOT_READER);
        }

        // 수락 상태 업데이트 및 수락일자 설정
        userMeeting.setIsAccepted(true);
        userMeeting.setAcceptedAt(LocalDateTime.now());

        // 업데이트된 UserMeeting 저장
        userMeetingRepository.save(userMeeting);

        // 모임의 capacity 에 참가자 인원을 추가
        int updatedCapacity = meeting.getCapacity() + userMeeting.getCapacity();
        meeting.setCapacity(updatedCapacity); // 새로운 capacity 설정

        // 변경된 Meeting 저장
        meetingRepository.save(meeting);

        log.info("모임 수락 처리 성공: meetingId {}, joinUserId {}", meetingId, joinUserId);
    }

    // 모임 거절
    public Boolean isRejection(Long userId, Long meetingId, Long joinUserId) {
        // 특정 모임과 사용자에 대한 UserMeeting entity 찾기
        UserMeeting userMeeting = userMeetingRepository.findByUser_UserIdAndMeeting_MeetingId(userId, meetingId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));

        // Meeting entity 가져오기
        Meeting meeting = userMeeting.getMeeting();

        // 모임장 검증: 현재 사용자가 모임의 작성자인지 확인
        if (!meeting.getUser().getUserId().equals(userId)) {
            throw new CustomException(ExceptionStatus.USER_NOT_READER);
        }

        // UserMeeting entity 삭제 (거절 처리)
        userMeetingRepository.delete(userMeeting);

        log.info("모임 거절 및 삭제 처리 성공: meetingId {}, joinUserId {}", meetingId, joinUserId);

        return true;
    }
}
