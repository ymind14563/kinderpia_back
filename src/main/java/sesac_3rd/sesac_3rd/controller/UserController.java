package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
//    @GetMapping("/status-with-code")
//    public ResponseEntity<?> getStatusWithCode() {
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "Success");
//        response.put("statusCode", HttpStatus.OK.value()); // 상태 코드 숫자 값 추가
//
//        return ResponseEntity.ok().body(response); // 200 OK 상태 코드와 본문을 함께 반환
//    }
    //////////////////////////////////////////////////
    // ResponseHandler 사용 예제->ResponseEntity<ApiResponse<List<User>>> 이런식으로 리턴값 명시
    // ResponseEntity<ApiResponse<User>>
// 전체 사용자 목록 조회
//@GetMapping("/users")
//public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
//    try {
//        List<User> users = userService.findAll();
//
//        ApiResponse<List<User>> response = new ApiResponse<>(
//                users,
//                HttpStatus.OK.value(),
//                "사용자 목록을 성공적으로 조회했습니다."
//        );
//
//        return ResponseEntity.ok(response);
//    } catch (Exception e) {
//        ApiResponse<List<User>> response = new ApiResponse<>(
//                null,
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "사용자 목록 조회 중 오류가 발생했습니다."
//        );
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(response);
//    }
//}

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @AuthenticationPrincipal 어노테이션은 Spring Security에서 현재 인증된 사용자의 정보를 컨트롤러에서 직접 접근할 수 있게 해주는 어노테이션

    // 로그인
    // 로그인 완료 후 리턴값을 뭘 해야할지는 정해야함
    @PostMapping("/login")
    public ResponseEntity<ResponseHandler<Boolean>> userLogin(@RequestBody LoginFormDTO dto){
        LoginResponse loginResponse = userService.userLogin(dto.getLoginId(), dto.getUserPw());

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                loginResponse.isLogined(),
                HttpStatus.OK.value(),
                "로그인 완료"
        );

        return ResponseEntity.ok().header("Authorization", "Bearer " + loginResponse.getToken()).body(response);
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseHandler<UserResponseDTO>> register(@RequestBody UserFormDTO dto){
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
    public ResponseEntity<ResponseHandler<Boolean>> checkNicknameDuplicate(@RequestBody UserFormDTO dto){
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
    public ResponseEntity<ResponseHandler<Boolean>> checkLoginIdDuplicate(@RequestBody UserFormDTO dto){
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
    public ResponseEntity<ResponseHandler<Boolean>> checkEmailDuplicate(@RequestBody UserFormDTO dto){
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
    public ResponseEntity<ResponseHandler<Boolean>> checkPhonenumDuplicate(@RequestBody UserFormDTO dto){
        userService.isPhonenumDuplicate(dto.getPhoneNum());

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                false,
                HttpStatus.OK.value(),   // 200
                "사용 가능한 전화번호입니다."
        );
        return ResponseEntity.ok(response);
    }

    // 로그아웃
//    void logout();

    // 회원 정보 단건 조회
    // security로 처리해야 할수도???
    // 그리고 params에 userId 노출은 웬만하면 안하는 걸로 바꾸는게 나을듯
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseHandler<UserDTO>> getUser(@PathVariable Long userId){
            UserDTO user = userService.getUser(userId);

            ResponseHandler<UserDTO> response = new ResponseHandler<>(
                    user,
                    HttpStatus.OK.value(),   // 200
                    "사용자 조회 완료"
            );

            return ResponseEntity.ok(response);
    }

    // 회원 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<ResponseHandler<UserDTO>> updateUser(@PathVariable Long userId
                                                                , @RequestBody UserFormDTO dto
//                                                                , @RequestPart("user") UserFormDTO dto
//  이미지는 선택                                                   , @RequestPart(value = "image", required = false) MultipartFile image
                                                                )
    {
            UserDTO updatedUser = userService.updateUser(userId, dto);
            ResponseHandler<UserDTO> response = new ResponseHandler<>(
                    updatedUser,
                    HttpStatus.OK.value(),  // 200
                    "사용자 수정 완료"
            );
            return ResponseEntity.ok(response);

    }

    // 이거도 body에서 userId빼오는 방식 말고 다른 방식으로 구현 가능성
    // 회원 탈퇴
    @PatchMapping("/logical/{userId}")
    public ResponseEntity<ResponseHandler<Boolean>> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                true,
                HttpStatus.OK.value(),   // 200
                "사용자 탈퇴 처리 완료"
        );

        return ResponseEntity.ok(response);
    }

    // 비밀번호 일치 확인 - ( 회원 수정, 탈퇴시 )
    @PostMapping("/check/userpw")
    public ResponseEntity<ResponseHandler<Boolean>> checkUserPw(@RequestBody LoginFormDTO dto){
        userService.checkUserPw(dto.getUserId(), dto.getUserPw());

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                true,
                HttpStatus.OK.value(),    // 200
                "비밀번호 일치"
        );

        return ResponseEntity.ok(response);
    }

    // 사용자 리뷰 목록 조회(장소 정보까지 같이)
    // 페이지네이션 필요할지도??
    @GetMapping("/review/list/{userId}")
    public ResponseEntity<ResponseHandler<List<UserReviewDTO.UserReviewListDTO>>> getUserReviewList(@PathVariable Long userId){
        List<UserReviewDTO.UserReviewListDTO> getReviewList = userService.getUserReviewList(userId);

        return ResponseHandler.response(getReviewList, HttpStatus.OK, "사용자 리뷰 목록 조회");
    }

    // 사용자 모임 일정 목록 조회(사용자가 모임장이거나 속해있는 모임, 삭제된 모임 제외)
    @GetMapping("/meetingTime/list/{userId}")
    public ResponseEntity<ResponseHandler<List<UserMeetingListDTO>>> getUserMeetingScheduleList(@PathVariable Long userId){
        List<UserMeetingListDTO> getUserMettingSchlist = userService.getUserMeetingScheduleList(userId);

        return ResponseHandler.response(getUserMettingSchlist, HttpStatus.OK, "사용자 모임 일정 목록 조회");
    }

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장이거나 모임에 속해 있는 경우) - 페이지네이션
    @GetMapping("/meeting/list/{userId}")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<UserMeetingListDTO>>> getUserMeetingList(@PathVariable Long userId,
                                                                                                         @RequestParam(defaultValue = "0") int page,
                                                                                                         @RequestParam(defaultValue = "8") int size)
    {
        PaginationResponseDTO<UserMeetingListDTO> getUserMeetingList = userService.getUserMeetingList(userId, size, page);

        return ResponseHandler.response(getUserMeetingList, HttpStatus.OK, "사용자 모임 목록 조회");
    }

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장인 모임) - 페이지네이션
    @GetMapping("/meeting/leader/list/{userId}")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<UserMeetingListDTO>>> getUserLeaderMeetingList(@PathVariable Long userId,
                                                                                                               @RequestParam(defaultValue = "0") int page,
                                                                                                               @RequestParam(defaultValue = "8") int size
                                                                                                               ){
        PaginationResponseDTO<UserMeetingListDTO> getUserLeaderMeetingList = userService.getUserLeaderMeetingList(userId, size, page);

        return ResponseHandler.response(getUserLeaderMeetingList, HttpStatus.OK, "사용자 모임 목록 조회");
    }

}
