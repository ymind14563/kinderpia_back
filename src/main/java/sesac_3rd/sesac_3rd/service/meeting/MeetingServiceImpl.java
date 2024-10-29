package sesac_3rd.sesac_3rd.service.meeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.repository.MeetingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    // 모임 목록 (default - 최신순 정렬)
    @Override
    public List<MeetingDTO> getAllMeetings() {
        List<Meeting> meetings = meetingRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        if (meetings.isEmpty()) {
            throw new CustomException(ExceptionStatus.MEETING_NOT_FOUND);
        }

        List<MeetingDTO> meetingDTOS = new ArrayList<>();

        for (Meeting meeting : meetings) {
            MeetingDTO meetingDTO = MeetingDTO.builder()
                    .meetingId(meeting.getMeetingId())
                    .placeId(meeting.getPlace().getPlaceId())
                    .meetingCategory(meeting.getMeetingCategory().getMeetingCtgId()) // 카테고리
                    .meetingTitle(meeting.getMeetingTitle()) // 타이틀
                    .capacity(meeting.getCapacity()) // 참가인원
                    .meetingLocation(meeting.getMeetingLocation()) // 모임장소
                    .meetingTime(meeting.getMeetingTime()) // 모임일시
                    .meetingStatus(meeting.getMeetingStatus()) // 모임 상태
                    .createdAt(meeting.getCreatedAt()) // 생성일자
                    .build();

            meetingDTOS.add(meetingDTO);
        }
        return meetingDTOS;
    }

    // 모임 목록 (open - 열려있는것만 정렬)
    @Override
    public List<MeetingDTO> getOpenMeetings() {
        List<Meeting> meetings = meetingRepository.findByMeetingStatus(MeetingStatus.ONGOING, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (meetings.isEmpty()) {
            throw new CustomException(ExceptionStatus.MEETING_NOT_FOUND);
        }

        List<MeetingDTO> meetingDTOS = new ArrayList<>();

        for (Meeting meeting : meetings) {
            MeetingDTO meetingDTO = MeetingDTO.builder()
                    .meetingId(meeting.getMeetingId())
                    .placeId(meeting.getPlace().getPlaceId())
                    .meetingCategory(meeting.getMeetingCategory().getMeetingCtgId()) // 카테고리
                    .meetingTitle(meeting.getMeetingTitle()) // 타이틀
                    .capacity(meeting.getCapacity()) // 참가인원
                    .meetingLocation(meeting.getMeetingLocation()) // 모임장소
                    .meetingTime(meeting.getMeetingTime()) // 모임일시
                    .meetingStatus(meeting.getMeetingStatus()) // 모임 상태
                    .createdAt(meeting.getCreatedAt()) // 생성일자
                    .build();

            meetingDTOS.add(meetingDTO);
        }
        return meetingDTOS;
    }

    // 키워드로 타이틀과 장소 검색
    @Override
    public List<MeetingDTO> searchMeetingsByKeyword(String keyword) {
        List<Meeting> meetings = meetingRepository.findByMeetingTitleContainingOrMeetingLocationContaining(
                keyword, keyword, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (meetings.isEmpty()) {
            throw new CustomException(ExceptionStatus.MEETING_NOT_FOUND);
        }

        List<MeetingDTO> meetingDTOS = new ArrayList<>();

        for (Meeting meeting : meetings) {
            MeetingDTO meetingDTO = MeetingDTO.builder()
                    .meetingId(meeting.getMeetingId())
                    .placeId(meeting.getPlace().getPlaceId())
                    .meetingCategory(meeting.getMeetingCategory().getMeetingCtgId()) // 카테고리
                    .meetingTitle(meeting.getMeetingTitle()) // 타이틀
                    .capacity(meeting.getCapacity()) // 참가인원
                    .meetingLocation(meeting.getMeetingLocation()) // 모임장소
                    .meetingTime(meeting.getMeetingTime()) // 모임일시
                    .meetingStatus(meeting.getMeetingStatus()) // 모임 상태
                    .createdAt(meeting.getCreatedAt()) // 생성일자
                    .build();

            meetingDTOS.add(meetingDTO);
        }
        return meetingDTOS;
    }
}
