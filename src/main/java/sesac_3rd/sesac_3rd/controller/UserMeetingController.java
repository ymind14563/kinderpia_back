package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.config.security.TokenProvider;
import sesac_3rd.sesac_3rd.dto.usermeeting.UserMeetingJoinDTO;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.service.usermeeting.UserMeetingService;

@RestController
@RequestMapping("/api/userMeeting")
public class UserMeetingController {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserMeetingService userMeetingService;

    // 모임 가입
    @PostMapping("/join/{meetingId}")
    public ResponseEntity<ResponseHandler<Void>> joinMeeting(
            @AuthenticationPrincipal Long userId,
            @PathVariable("meetingId") Long meetingId,
            @RequestBody UserMeetingJoinDTO userMeetingJoinDTO) {
        try {
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId 토큰없음 >> " + userId);
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId 토큰있음 >> " + userId);

            userMeetingService.joinMeeting(userId, meetingId, userMeetingJoinDTO);

            ResponseHandler<Void> response = new ResponseHandler<>(
                    null,
                    HttpStatus.NO_CONTENT.value(), // 204
                    "모임 가입 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 모임 탈퇴
    @DeleteMapping("/exit/{meetingId}")
    public ResponseEntity<ResponseHandler<Boolean>> exitMeeting(
            @AuthenticationPrincipal Long userId,
            @PathVariable("meetingId") Long meetingId) {
        try {
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId 토큰없음 >> " + userId);
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId 토큰있음 >> " + userId);

            Boolean result = userMeetingService.exitMeeting(userId, meetingId);

            ResponseHandler<Boolean> response = new ResponseHandler<>(
                    result, // true
                    HttpStatus.CREATED.value(), // 201
                    "모임 탈퇴 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 모임 수락
    @PutMapping("/{meetingId}/accept/{joinUserId}")
    public ResponseEntity<ResponseHandler<Boolean>> acceptMeeting(
            @AuthenticationPrincipal Long userId, // 작성자의 userId
            @PathVariable("meetingId") Long meetingId,
            @PathVariable("joinUserId") Long joinUserId // 가입하는 사람의 userId
    ) {
        try {
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId 토큰없음 >> " + userId);
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId 토큰있음 >> " + userId);

            Boolean result = userMeetingService.isAccepted(userId, meetingId, joinUserId);

            ResponseHandler<Boolean> response = new ResponseHandler<>(
                    result, // true
                    HttpStatus.OK.value(), // 200
                    "모임 참가자 수락 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 모임 거절
    @DeleteMapping("/{meetingId}/reject/{joinUserId}")
    public ResponseEntity<ResponseHandler<Boolean>> rejectMeeting(
            @AuthenticationPrincipal Long userId, // 작성자의 userId
            @PathVariable("meetingId") Long meetingId,
            @PathVariable("joinUserId") Long joinUserId // 가입하는 사람의 userId
    ) {
        try {
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId 토큰없음 >> " + userId);
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId 토큰있음 >> " + userId);

            Boolean result = userMeetingService.isRejection(userId, meetingId, joinUserId);

            ResponseHandler<Boolean> response = new ResponseHandler<>(
                    result, // true
                    HttpStatus.OK.value(), // 200
                    "모임 참가자 거절 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
