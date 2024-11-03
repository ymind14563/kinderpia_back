package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.dto.usermeeting.UserMeetingJoinDTO;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.service.usermeeting.UserMeetingService;

@RestController
@RequestMapping("/api/userMeeting")
public class UserMeetingController {
    @Autowired
    private UserMeetingService userMeetingService;

    // 모임 가입
    @PostMapping("/join/{meetingId}")
    public ResponseEntity<ResponseHandler<Void>> joinMeeting(
            @PathVariable("meetingId") Long meetingId,
            @RequestBody UserMeetingJoinDTO userMeetingJoinDTO) {
        try {
            userMeetingService.joinMeeting(meetingId, userMeetingJoinDTO);

            ResponseHandler<Void> response = new ResponseHandler<>(
                    null,
                    HttpStatus.CREATED.value(), // 201
                    "모임 가입 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 모임 탈퇴
    @PostMapping("/exit/{meetingId}")
    public ResponseEntity<ResponseHandler<Void>> exitMeeting(@PathVariable("meetingId") Long meetingId) {
        try {
            userMeetingService.exitMeeting(meetingId);

            ResponseHandler<Void> response = new ResponseHandler<>(
                    null,
                    HttpStatus.CREATED.value(), // 201
                    "모임 탈퇴 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
