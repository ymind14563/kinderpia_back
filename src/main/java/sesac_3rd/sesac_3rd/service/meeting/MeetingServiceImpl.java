package sesac_3rd.sesac_3rd.service.meeting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingFormDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingStatusDTO;
import sesac_3rd.sesac_3rd.dto.usermeeting.UserMeetingJoinDTO;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.entity.UserMeeting;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.mapper.meeting.MeetingMapper;
import sesac_3rd.sesac_3rd.mapper.usermeeting.UserMeetingMapper;
import sesac_3rd.sesac_3rd.repository.MeetingRepository;
import sesac_3rd.sesac_3rd.repository.PlaceRepository;
import sesac_3rd.sesac_3rd.repository.UserMeetingRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomRepository;
import sesac_3rd.sesac_3rd.service.chat.ChatRoomService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserMeetingRepository userMeetingRepository;

    // 모임 목록 (default - 최신순 정렬)
    @Override
    public PaginationResponseDTO<MeetingDTO> getAllMeetings(Pageable pageable) {
        // 정렬 설정
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Meeting> meetings = meetingRepository.findAll(sortedPageable);

        if (meetings.isEmpty()) {
            throw new CustomException(ExceptionStatus.MEETING_NOT_FOUND);
        }

        List<MeetingDTO> meetingDTOS = new ArrayList<>();

        for (Meeting meeting : meetings) {
            MeetingDTO meetingDTO = MeetingMapper.toMeetingDTO(meeting);
            meetingDTOS.add(meetingDTO);
        }

        log.info("모임 목록 조회[전체] 성공: 총 {}건", meetingDTOS.size());

        return new PaginationResponseDTO<>(meetingDTOS, meetings);
    }

    // 모임 목록 (더보기: 모임 시간순으로 정렬 + deleted 제외)
    @Override
    public PaginationResponseDTO<MeetingDTO> getMeetings(Pageable pageable) {
        // 정렬 설정
        Sort sort = Sort.by(Sort.Direction.DESC, "meetingTime");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Meeting> meetings = meetingRepository.findByMeetingStatusNotDeleted(sortedPageable);

        if (meetings.isEmpty()) {
            throw new CustomException(ExceptionStatus.MEETING_NOT_FOUND);
        }

        List<MeetingDTO> meetingDTOS = new ArrayList<>();

        for (Meeting meeting : meetings) {
            MeetingDTO meetingDTO = MeetingMapper.toMeetingDTO(meeting);
            meetingDTOS.add(meetingDTO);
        }

        log.info("모임 목록 조회[deleted 제외] 성공: 총 {}건", meetingDTOS.size());

        return new PaginationResponseDTO<>(meetingDTOS, meetings);
    }

    // 모임 목록 (open - 열려있는 것만 정렬 + 모임 시간순으로 정렬)
    @Override
    public PaginationResponseDTO<MeetingDTO> getOpenMeetings(Pageable pageable) {
        // 정렬 meetingTime
        Sort sort = Sort.by(Sort.Direction.DESC, "meetingTime");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Meeting> meetings = meetingRepository.findByMeetingStatus(MeetingStatus.ONGOING, sortedPageable);

        if (meetings.isEmpty()) {
            throw new CustomException(ExceptionStatus.MEETING_NOT_FOUND);
        }

        List<MeetingDTO> meetingDTOS = new ArrayList<>();

        for (Meeting meeting : meetings) {
            MeetingDTO meetingDTO = MeetingMapper.toMeetingDTO(meeting);
            meetingDTOS.add(meetingDTO);
        }

        log.info("모임 목록 조회[open] 성공: 총 {}건", meetingDTOS.size());

        return new PaginationResponseDTO<>(meetingDTOS, meetings);
    }

    // 키워드로 타이틀과 장소 검색 (모임 시간순으로 정렬)
    @Override
    public PaginationResponseDTO<MeetingDTO> searchMeetingsByKeyword(String keyword, Pageable pageable) {
        // 정렬 meetingTime
        Sort sort = Sort.by(Sort.Direction.DESC, "meetingTime");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Meeting> meetings = meetingRepository.findByMeetingTitleOrDistrict(
                keyword, sortedPageable);

        if (meetings.isEmpty()) {
            throw new CustomException(ExceptionStatus.MEETING_NOT_FOUND);
        }

        List<MeetingDTO> meetingDTOS = new ArrayList<>();

        for (Meeting meeting : meetings) {
            MeetingDTO meetingDTO = MeetingMapper.toMeetingDTO(meeting);
            meetingDTOS.add(meetingDTO);
        }

        log.info("모임 목록 조회[keyword] 성공: 총 {}건", meetingDTOS.size());

        return new PaginationResponseDTO<>(meetingDTOS, meetings);
    }

    // 모임 상세조회 (profile_img, chatroom_id 포함)
    @Override
    public MeetingDetailDTO getDetailMeeting(Long meetingId) {
        // Meeting 조회 (모임장 정보 포함)
        Meeting meeting = meetingRepository.findByMeetingIdWithUserAndChatRoom(meetingId)
                .orElseThrow( () -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));

        log.info("모임 상세 조회 성공: 모임ID {}", meetingId);

        // entity to dto
        return MeetingMapper.toMeetingDetailDTO(meeting);
    }

    // 모임 생성
    @Override
    public Meeting createMeeting(Long userId, MeetingFormDTO meetingFormDTO) {
        meetingFormDTO.setUserId(userId);

        // 네이버 (모임장소)
        meetingFormDTO.setMeetingLocation(meetingFormDTO.getMeetingLocation());

        // 모임 생성
        Meeting meeting = MeetingMapper.toMeetingFormEntity(meetingFormDTO); // Meeting 엔티티 생성
        meeting.setCapacity(1); // 모임 생성 시 기본 참가 인원 1명으로 설정
        meeting.setAuthType(meetingFormDTO.isAuthType());
        meeting.setIsLimited(meetingFormDTO.getIsLimited());
        meetingRepository.save(meeting);

        log.info("authType: {}, limited: {}", meetingFormDTO.isAuthType(), meetingFormDTO.getIsLimited());

        // 채팅방 생성
        chatRoomService.createChatRoomIfNotExists(meeting.getMeetingId());

        // 모임 생성 후 자동 참가 처리
        UserMeetingJoinDTO userMeetingJoinDTO = new UserMeetingJoinDTO();
        userMeetingJoinDTO.setUserId(userId);
        userMeetingJoinDTO.setMeetingId(meeting.getMeetingId());
        userMeetingJoinDTO.setCapacity(1); // 참가자는 기본 1명으로 설정

        // UserMeetingJoinDTO 를 UserMeeting entity 로 변환
        UserMeeting userMeeting = UserMeetingMapper.toUserMeetingJoinEntity(userMeetingJoinDTO);
        userMeeting.setMeeting(meeting); // UserMeeting 에 Meeting entity 설정

        // 인증 여부에 따라 수락 상태 설정
        if (!meeting.isAuthType() || userId.equals(meeting.getUser().getUserId())) {
            userMeeting.setIsAccepted(true); // 자동 수락 (모임장인 경우 또는 인증된 경우)
        } else {
            userMeeting.setIsAccepted(null); // 인증 대기 상태
        }

        // UserMeeting entity 저장
        userMeetingRepository.save(userMeeting);

        log.info("모임 생성 및 참가 성공: 모임장ID {}", userId);

        return meeting; // meetingId 프론트로 반환하려고 meeting 리턴
    }

    // 모임 수정
    @Override
    public Boolean updateMeeting(Long userId, Long meetingId, MeetingFormDTO meetingFormDTO) {
        // meetingId 로 기존 Meeting entity 조회
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));

        // 모임장 검증
        if (!meeting.getUser().getUserId().equals(userId)) {
            throw new CustomException(ExceptionStatus.USER_NOT_READER);
        }

        System.out.println("userId >> " + userId + ", meetingId >> " + meetingId);

        // null 비교 후 전단된 값만 업데이트
        if (meetingFormDTO.getMeetingTitle() != null) {
            meeting.setMeetingTitle(meetingFormDTO.getMeetingTitle());
        }
        if (meetingFormDTO.getTotalCapacity() != -1) { // totalCapacity 가 -1이 아닌 경우에만 업데이트 (프론트에서 -1을 보내면 수정하지 않음)
            meeting.setTotalCapacity(meetingFormDTO.getTotalCapacity());
        }
        if (meetingFormDTO.getIsLimited() != null) {
            meeting.setIsLimited(meetingFormDTO.getIsLimited()); // 총원제한 여부 (true/false)
        }
        if (meetingFormDTO.getMeetingContent() != null) {
            meeting.setMeetingContent(meetingFormDTO.getMeetingContent());
        }
        meeting.setUpdatedAt(LocalDateTime.now()); // 수정일시

        // 변경된 entity 저장
        meetingRepository.save(meeting);

        log.info("모임 수정 성공: 수정된 모임ID {}", meetingId);

        return true;
    }

    // 모임삭제 (모임장이 삭제, 관리자가 삭제 : DELETED)
    @Override
    public Boolean deletedMeeting(Long userId, Long meetingId) {
        // meetingId 로 기존 Meeting entity 조회
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow( () -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));

        // 모임장 검증
        if (!userId.equals(meeting.getUser().getUserId())) {
            throw new CustomException(ExceptionStatus.USER_NOT_READER);
        }

        // meetingStatus 상태를 DELETED 로 설정
        meeting.setMeetingStatus(MeetingStatus.DELETED);

        // 업데이트된 entity 저장
        meetingRepository.save(meeting);

        // 채팅방 비활성화
        ChatRoom chatRoom = chatRoomRepository.findByMeetingId(meetingId);
        chatRoom.setIsActive(false);
        chatRoomRepository.save(chatRoom);

        log.info("모임 삭제(DELETED) 성공: 삭제된 모임ID {}", meetingId);

        return true;
    }

    // 모집완료 (인원마감, 모임장이 임의로 마감 : COMPLETED)
    @Override
    public Boolean completedMeeting(Long userId, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));

        if (!userId.equals(meeting.getUser().getUserId())) {
            throw new CustomException(ExceptionStatus.USER_NOT_READER);
        }

        // meetingStatus 상태를 COMPLETED 로 설정
        meeting.setMeetingStatus(MeetingStatus.COMPLETED);

        meetingRepository.save(meeting);

        log.info("모집 완료(COMPLETED) 성공: 모집완료된 모임ID {}", meetingId);

        return false;
    }

    // meetingStatus 상태
    @Override
    public MeetingStatusDTO meetingStatus(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));

        // 현재 참가 인원이 최대 인원을 초과하는 경우, 상태를 COMPLETED 로 설정
        if (meeting.getCapacity() >= meeting.getTotalCapacity()) {
            meeting.setMeetingStatus(MeetingStatus.COMPLETED); // meetingStatus 상태를 COMPLETED 로 설정
            meetingRepository.save(meeting);
        }

        // 모임 기간이 종료되었을 경우, 상태를 END 로 설정
        if (meeting.getMeetingTime().isBefore(LocalDateTime.now())) { // 현재 시간보다 이전인지 비교
            meeting.setMeetingStatus(MeetingStatus.END); // meetingStatus 상태를 END 로 설정
            meetingRepository.save(meeting);
        }

        log.info("meetingStatus 상태 확인: 모임ID {}", meetingId);

        return MeetingMapper.toMeetingStatusDTO(meeting);
    }
}