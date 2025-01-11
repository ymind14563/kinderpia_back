package sesac_3rd.sesac_3rd.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sesac_3rd.sesac_3rd.dto.user.*;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 로그인
    // 로그인 완료 후 리턴값을 뭘 해야할지는 정해야함
    @PostMapping("/login")
    public ResponseEntity<ResponseHandler<Boolean>> userLogin(@RequestBody LoginFormDTO dto, HttpServletResponse httpResponse) {
        LoginResponse loginResponse = userService.userLogin(dto.getLoginId(), dto.getUserPw());

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                loginResponse.isLogined(),
                HttpStatus.OK.value(),
                "로그인 완료"
        );

        // JWT 쿠키 생성
        Cookie jwtCookie = new Cookie("jwt", loginResponse.getToken());
        jwtCookie.setHttpOnly(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(3600);
        // jwtCookie.setSecure(true);

        // HttpServletResponse에 쿠키 추가
        httpResponse.addCookie(jwtCookie);

        return ResponseEntity.ok().header("Authorization", "Bearer " + loginResponse.getToken()).body(response);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ResponseHandler<UserResponseDTO>> register(@RequestBody UserFormDTO dto) {
        UserResponseDTO registeredUser = userService.register(dto);
        ResponseHandler<UserResponseDTO> response = new ResponseHandler<>(
                registeredUser,
                HttpStatus.CREATED.value(),   // 201
                "회원가입 완료"
        );
        return ResponseEntity.ok(response);
    }

    // 회원가입 - 닉네임 중복 검사
    @PostMapping("/check/nickname")
    public ResponseEntity<ResponseHandler<Boolean>> checkNicknameDuplicate(@RequestBody UserFormDTO dto) {
        userService.isNicknameDuplicate(dto.getNickname());

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                false,
                HttpStatus.OK.value(),   // 200
                "사용 가능한 닉네임입니다."
        );
        return ResponseEntity.ok(response);
    }

    // 회원가입 - 아이디 중복 검사
    @PostMapping("/check/loginId")
    public ResponseEntity<ResponseHandler<Boolean>> checkLoginIdDuplicate(@RequestBody UserFormDTO dto) {
        userService.isLoginIdDuplicate(dto.getLoginId());

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                false,
                HttpStatus.OK.value(),   // 200
                "사용 가능한 사용자 아이디입니다."
        );
        return ResponseEntity.ok(response);
    }

    // 회원가입 - 이메일 중복 검사
    @PostMapping("/check/email")
    public ResponseEntity<ResponseHandler<Boolean>> checkEmailDuplicate(@RequestBody UserFormDTO dto) {
        userService.isEmailDuplicate(dto.getEmail());

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                false,
                HttpStatus.OK.value(),   // 200
                "사용 가능한 이메일 입니다."
        );
        return ResponseEntity.ok(response);
    }

    // 회원가입 - 전화번호 중복 검사
    @PostMapping("/check/phoneNum")
    public ResponseEntity<ResponseHandler<Boolean>> checkPhonenumDuplicate(@RequestBody UserFormDTO dto) {
        userService.isPhonenumDuplicate(dto.getPhoneNum());

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                false,
                HttpStatus.OK.value(),   // 200
                "사용 가능한 전화번호입니다."
        );
        return ResponseEntity.ok(response);
    }

    // 회원 정보 단건 조회
    @GetMapping
    public ResponseEntity<ResponseHandler<UserDTO>> getUser(@AuthenticationPrincipal Long userId) {
        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        UserDTO user = userService.getUser(userId);

        ResponseHandler<UserDTO> response = new ResponseHandler<>(
                user,
                HttpStatus.OK.value(),   // 200
                "사용자 조회 완료"
        );

        return ResponseEntity.ok(response);
    }

    // 회원 정보 수정
    @PutMapping
    public ResponseEntity<ResponseHandler<UserDTO>> updateUser(@AuthenticationPrincipal Long userId
            , @RequestBody UserFormDTO dto
    ) {
        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        UserDTO updatedUser = userService.updateUser(userId, dto);
        ResponseHandler<UserDTO> response = new ResponseHandler<>(
                updatedUser,
                HttpStatus.OK.value(),  // 200
                "사용자 수정 완료"
        );
        return ResponseEntity.ok(response);

    }

    // 회원 정보 수정(프로필 이미지)
    @PutMapping("/profileImg")
    public ResponseEntity<ResponseHandler<UserDTO>> updateUserProfileImg(@AuthenticationPrincipal Long userId
            , @RequestPart(value = "image") MultipartFile image
    ) {
        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        UserDTO updatedUser = userService.updateUserProfileImg(userId, image);
        ResponseHandler<UserDTO> response = new ResponseHandler<>(
                updatedUser,
                HttpStatus.OK.value(),  // 200
                "사용자 프로필 수정 완료"
        );
        return ResponseEntity.ok(response);

    }

    // 회원 탈퇴
    @PatchMapping("/logical")
    public ResponseEntity<ResponseHandler<Boolean>> deleteUser(@AuthenticationPrincipal Long userId) {
        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        userService.deleteUser(userId);

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                true,
                HttpStatus.OK.value(),   // 200
                "사용자 탈퇴 처리 완료"
        );

        return ResponseEntity.ok(response);
    }

    // 비밀번호 일치 확인 - ( 회원 수정, 탈퇴시 )
    @PostMapping("/check/userPw")
    public ResponseEntity<ResponseHandler<Boolean>> checkUserPw(@AuthenticationPrincipal Long userId, @RequestBody LoginFormDTO dto) {
        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        userService.checkUserPw(userId, dto.getUserPw());

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                true,
                HttpStatus.OK.value(),    // 200
                "비밀번호 일치"
        );

        return ResponseEntity.ok(response);
    }

    // 사용자 리뷰 목록 조회(장소 정보까지 같이)
    @GetMapping("/review/list")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<UserReviewDTO>>> getUserReviewList(@AuthenticationPrincipal Long userId,
                                                                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                   @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        PaginationResponseDTO<UserReviewDTO> getReviewList = userService.getUserReviewList(userId, size, page);

        return ResponseHandler.response(getReviewList, HttpStatus.OK, "사용자 리뷰 목록 조회");
    }

    // 사용자 모임 일정 목록 조회(사용자가 모임장이거나 속해있는 모임, 삭제된 모임 제외)
    @GetMapping("/meetingTime/list")
    public ResponseEntity<ResponseHandler<List<UserMeetingTimeListDTO>>> getUserMeetingScheduleList(@AuthenticationPrincipal Long userId) {
        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        List<UserMeetingTimeListDTO> getUserMeetingSchlist = userService.getUserMeetingScheduleList(userId);

        return ResponseHandler.response(getUserMeetingSchlist, HttpStatus.OK, "사용자 모임 일정 목록 조회");
    }

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장이거나 모임에 속해 있는 경우) - 페이지네이션
    @GetMapping("/meeting/list")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<UserMeetingListDTO>>> getUserMeetingList(@AuthenticationPrincipal Long userId,
                                                                                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                         @RequestParam(name = "size", defaultValue = "10") int size) {
        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        PaginationResponseDTO<UserMeetingListDTO> getUserMeetingList = userService.getUserMeetingList(userId, size, page);

        return ResponseHandler.response(getUserMeetingList, HttpStatus.OK, "사용자 모임 목록 조회");
    }

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장인 모임) - 페이지네이션
    @GetMapping("/meeting/leader/list")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<UserMeetingListDTO>>> getUserLeaderMeetingList(@AuthenticationPrincipal Long userId,
                                                                                                               @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                               @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        PaginationResponseDTO<UserMeetingListDTO> getUserLeaderMeetingList = userService.getUserLeaderMeetingList(userId, size, page);

        return ResponseHandler.response(getUserLeaderMeetingList, HttpStatus.OK, "사용자 모임장 목록 조회");
    }

    // 모임 상태는 모임에 참여 여부, 가입 신청 전/수락 전/수락 후(참여) -> 신청 여부, 수락 여부, 신고 여부
    // 모임상세 접근시 사용자 상태 조회(신고여부, 신청여부, 수락여부)
    @PostMapping("/meeting/status")
    public ResponseEntity<ResponseHandler<UserMeetingStatusDTO>> getUserMeetingStatus(@RequestBody UserMeetingStatusDTO dto) {
        UserMeetingStatusDTO statusDTO = userService.getUserMeetingStatus(dto.getUserId(), dto.getMeetingId());

        return ResponseHandler.response(statusDTO, HttpStatus.OK, "사용자 모임 상태 조회");
    }

    // 사용자가 모임장인 모임들의 승인 대기자 목록(각각 모임에 대한)
    @GetMapping("/meeting/{meetingId}/pending-approvals")

    public ResponseEntity<ResponseHandler<List<UserResponseDTO>>> getUserMeetingApprovalList(@PathVariable("meetingId") Long meetingId){
        List<UserResponseDTO> getApproval = userService.getUserMeetingApprovalList(meetingId);
        return ResponseHandler.response(getApproval, HttpStatus.OK, "승인 대기자 목록 조회");
    }

}
