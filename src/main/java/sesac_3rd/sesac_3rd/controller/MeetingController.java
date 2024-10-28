package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.service.meeting.MeetingService;

import java.util.List;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    // 모임 목록 (default - 최신순 정렬)
    @GetMapping("/list")
    public List<MeetingDTO> getMeetingList() {
        return meetingService.getAllMeetings();
    }

    // 모임 목록 (open - 열려있는것만 정렬)
    @GetMapping("/list/open")
    public List<MeetingDTO> getOpenMeetingList() {
        return meetingService.getOpenMeetings();
    }

    // 키워드로 모임 검색
    @GetMapping("/search")
    public List<MeetingDTO> searchMeetings(@RequestParam String keyword) {
        return meetingService.searchMeetingsByKeyword(keyword);
    }
}

