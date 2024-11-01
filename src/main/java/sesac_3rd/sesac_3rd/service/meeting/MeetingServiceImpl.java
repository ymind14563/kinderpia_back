package sesac_3rd.sesac_3rd.service.meeting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingFormDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.mapper.meeting.MeetingMapper;
import sesac_3rd.sesac_3rd.repository.MeetingRepository;
import sesac_3rd.sesac_3rd.repository.PlaceRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private PlaceRepository placeRepository;

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

    // 모임 목록 (open - 열려있는 것만 정렬)
    @Override
    public PaginationResponseDTO<MeetingDTO> getOpenMeetings(Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.findByMeetingStatus(MeetingStatus.ONGOING, pageable);

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

    // 키워드로 타이틀과 장소 검색
    @Override
    public PaginationResponseDTO<MeetingDTO> searchMeetingsByKeyword(String keyword, Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.findByMeetingTitleOrLocation(
                keyword, pageable);

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

    // 모임 상세 조회 (모임장 정보 포함)
    @Override
    public MeetingDetailDTO getDetailMeeting(Long meetingId) {
        // Meeting 조회 (모임장 정보 포함)
        Meeting meeting = meetingRepository.findByMeetingIdWithUser(meetingId)
                .orElseThrow( () -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));

        // Meeting 정보를 통해 DTO 변환
        return MeetingMapper.toMeetingDetailDTO(meeting);
    }

    // 모임 생성 (placeId 조회해서 넣기 포함)
    public void createMeeting(Long userId, MeetingFormDTO meetingFormDTO) {
        // 모임장 ID
        meetingFormDTO.setUserId(userId); // userId를 DTO 에 직접 설정
        // Place 조회
        Place place = placeRepository.findById(meetingFormDTO.getPlaceId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.PLACE_NOT_FOUND));
        // dto to entity
        Meeting meeting = MeetingMapper.toMeetingFormEntity(meetingFormDTO); // Meeting 엔티티 생성
        meeting.setPlace(place); // Place 엔티티 설정
        // insert
        meetingRepository.save(meeting);

        // 채팅방 생성 로직...

        log.info("모임 생성 성공: 모임장ID {}", userId);
    }
}
