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

@Slf4j
@Service
public class UserMeetingServiceImpl implements UserMeetingService {
    @Autowired
    UserMeetingRepository userMeetingRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MeetingService meetingService;

    // 모임 참가
    public void joinMeeting(UserMeetingJoinDTO userMeetingJoinDTO) {
        // 임시로 userId 설정
        Long userId = 3L; // JWT 없이 임시로 설정한 userId
        userMeetingJoinDTO.setUserId(userId);

        // 이미 참가한 사용자 여부 확인
        if (userMeetingRepository.existsByUser_UserIdAndMeeting_MeetingId(userId, userMeetingJoinDTO.getMeetingId())) {
            throw new CustomException(ExceptionStatus.MEETING_ALREADY_JOINED);
        }

        // 인증여부 조회
        MeetingDetailDTO meetingDetailDTO = meetingService.getDetailMeeting(userMeetingJoinDTO.getMeetingId());

        // UserMeeting 엔티티 생성
        UserMeeting userMeeting = UserMeetingMapper.toUserMeetingJoinEntity(userMeetingJoinDTO);

        // Meeting 엔티티 조회 후 UserMeeting 에 설정
        Meeting meeting = meetingRepository.findById(userMeetingJoinDTO.getMeetingId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));
        userMeeting.setMeeting(meeting); // Meeting 객체 설정

        // 인증 여부에 따라 수락 상태 설정
        if (!meetingDetailDTO.isAuthType()) {
            userMeeting.setIsAccepted(null); // 인증 대기 상태로 설정
        } else {
            userMeeting.setIsAccepted(true); // 자동 수락
        }

        // UserMeeting 엔티티 저장
        userMeetingRepository.save(userMeeting);

        // 기존 capacity 에 입력된 capacity 값을 더한 후 업데이트
        int updatedCapacity = meeting.getCapacity() + userMeetingJoinDTO.getCapacity();
        meeting.setCapacity(updatedCapacity); // 새로운 capacity 설정
        meetingRepository.save(meeting); // 변경된 Meeting 저장

        log.info("모임 참가 성공: 참가한 userId {}", userId);
    }
}
