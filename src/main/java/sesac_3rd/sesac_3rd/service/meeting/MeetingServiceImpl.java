package sesac_3rd.sesac_3rd.service.meeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.repository.MeetingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    // 모임 목록 (전체)
    @Override
    public List<MeetingDTO> getAllMeetings() {
        List<Meeting> meetings = meetingRepository.findAll();

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
                    .build();

            meetingDTOS.add(meetingDTO);
        }
        return meetingDTOS;
    }
}
