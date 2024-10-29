package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.dto.user.LoginFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.dto.user.UserFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserResponseDTO;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.service.user.UserService;

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

    // 로그인
    // 로그인 완료 후 리턴값을 뭘 해야할지는 정해야함
    @PostMapping("/login")
    public ResponseEntity<ResponseHandler<LoginFormDTO>> userLogin(@RequestBody LoginFormDTO dto){
        LoginFormDTO formDTO = userService.userLogin(dto.getLoginId(), dto.getUserPw());

        ResponseHandler<LoginFormDTO> response = new ResponseHandler<>(
                formDTO,
                HttpStatus.OK.value(),
                "로그인 완료"
        );

        return ResponseEntity.ok(response);
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
    public ResponseEntity<ResponseHandler<Boolean>> checkNicknameDuplicate(@RequestBody String nickname){
        userService.isNicknameDuplicate(nickname);

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                false,
                HttpStatus.OK.value(),   // 200
                "사용 가능한 닉네임입니다."
        );
        return ResponseEntity.ok(response);
    }

    // 회원가입 - 아이디 중복 검사
    @PostMapping("/check/loginid")
    public ResponseEntity<ResponseHandler<Boolean>> checkLoginIdDuplicate(@RequestBody String loginId){
        userService.isLoginIdDuplicate(loginId);

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                false,
                HttpStatus.OK.value(),   // 200
                "사용 가능한 사용자 아이디입니다."
        );
        return ResponseEntity.ok(response);
    }

    // 회원가입 - 이메일 중복 검사
    @PostMapping("/check/email")
    public ResponseEntity<ResponseHandler<Boolean>> checkEmailDuplicate(@RequestBody String email){
        userService.isEmailDuplicate(email);

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                false,
                HttpStatus.OK.value(),   // 200
                "사용 가능한 이메일 입니다."
        );
        return ResponseEntity.ok(response);
    }

    // 회원가입 - 전화번호 중복 검사
    @PostMapping("/check/phonenum")
    public ResponseEntity<ResponseHandler<Boolean>> checkPhonenumDuplicate(@RequestBody String phoneNum){
        userService.isPhonenumDuplicate(phoneNum);

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
    public ResponseEntity<ResponseHandler<UserDTO>> updateUser(@PathVariable Long userId, @RequestBody UserFormDTO dto){
        try {
            UserDTO updatedUser = userService.updateUser(userId, dto);
            ResponseHandler<UserDTO> response = new ResponseHandler<>(
                    updatedUser,
                    HttpStatus.OK.value(),  // 200
                    "사용자 수정 완료"
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
}
