package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.service.meeting.MeetingService;

import java.util.List;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    // 모임 목록 (default - 최신순 정렬)
    @GetMapping("/list")
    public ResponseEntity<ResponseHandler<List<MeetingDTO>>> getAllMeetings() {
        try {
            List<MeetingDTO> meetings = meetingService.getAllMeetings();

            ResponseHandler<List<MeetingDTO>> response = new ResponseHandler<>(
                    meetings,
                    HttpStatus.OK.value(), // 200
                    "모임 목록 조회[전체] 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 모임 목록 (open - 열려있는것만 정렬)
    @GetMapping("/list/open")
    public ResponseEntity<ResponseHandler<List<MeetingDTO>>> getOpenMeetings() {
        try {
            List<MeetingDTO> meetings = meetingService.getOpenMeetings();

            ResponseHandler<List<MeetingDTO>> response = new ResponseHandler<>(
                    meetings,
                    HttpStatus.OK.value(), // 200
                    "모임 목록 조회[open] 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 키워드로 모임 검색
    @GetMapping("/list/search")
    public ResponseEntity<ResponseHandler<List<MeetingDTO>>> getSearchMeetingsByKeyword(@RequestParam String keyword) {
        try {
            List<MeetingDTO> meetings = meetingService.searchMeetingsByKeyword(keyword);

            ResponseHandler<List<MeetingDTO>> response = new ResponseHandler<>(
                    meetings,
                    HttpStatus.OK.value(), // 200
                    "모임 목록 조회[keyword] 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

