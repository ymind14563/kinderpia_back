package sesac_3rd.sesac_3rd.service.meeting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.mapper.meeting.MeetingMapper;
import sesac_3rd.sesac_3rd.repository.MeetingRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
            MeetingDTO meetingDTO = MeetingMapper.toMeetingDTO(meeting);
            meetingDTOS.add(meetingDTO);
        }

        log.info("모임 목록 조회[전체] 성공: 총 {}건", meetingDTOS.size());

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
            MeetingDTO meetingDTO = MeetingMapper.toMeetingDTO(meeting);
            meetingDTOS.add(meetingDTO);
        }

        log.info("모임 목록 조회[open] 성공: 총 {}건", meetingDTOS.size());

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
            MeetingDTO meetingDTO = MeetingMapper.toMeetingDTO(meeting);
            meetingDTOS.add(meetingDTO);
        }

        log.info("모임 목록 조회[keyword] 성공: 총 {}건", meetingDTOS.size());

        return meetingDTOS;
    }
}
