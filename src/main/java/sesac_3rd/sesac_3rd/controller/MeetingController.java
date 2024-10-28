package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.service.meeting.MeetingService;

import java.util.List;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @GetMapping("/list")
    public List<MeetingDTO> getMeetingList() {
        return meetingService.getAllMeetings();
    }
}
