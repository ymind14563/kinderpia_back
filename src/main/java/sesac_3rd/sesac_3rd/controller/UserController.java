package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.dto.user.LoginFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.dto.user.UserFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserResponseDTO;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.service.user.UserService;

@RestController
@RequestMapping("/user")
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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseHandler> userLogin(@RequestBody LoginFormDTO dto){
        return null;
    }

    // 회원가입
    // 에러 표시 어떻게 되는지 확인 필요
    @PostMapping("/signup")
    public ResponseEntity<ResponseHandler<UserResponseDTO>> register(@RequestBody UserFormDTO dto){
        UserResponseDTO registeredUser = userService.register(dto);
        ResponseHandler<UserResponseDTO> response = new ResponseHandler<>(
                registeredUser,
                HttpStatus.OK.value(),   // 200
                "회원가입 완료"
        );
        return ResponseEntity.ok(response);
    }

    // 회원가입 - 닉네임 중복 검사
    @PostMapping("/check/nickname")
    public ResponseEntity<ResponseHandler<Boolean>> checkNicknameDuplicate(@RequestBody String nickname){
        boolean isDuplicated = userService.isNicknameDuplicate(nickname);

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                isDuplicated,
                isDuplicated ? HttpStatus.CONFLICT.value() : HttpStatus.OK.value(),   // 409, 200
                isDuplicated ? "이미 사용중인 닉네임" : "사용 가능한 닉네임"
        );
        return ResponseEntity.ok(response);
    }

    // 회원가입 - 아이디 중복 검사
    @PostMapping("/check/loginid")
    public ResponseEntity<ResponseHandler<Boolean>> checkLoginIdDuplicate(@RequestBody String loginId){
        boolean isDuplicated = userService.isLoginIdDuplicate(loginId);

        ResponseHandler<Boolean> response = new ResponseHandler<>(
                isDuplicated,
                isDuplicated ? HttpStatus.CONFLICT.value() : HttpStatus.OK.value(),   // 409, 200
                isDuplicated ? "이미 사용중인 사용자 아이디" : "사용 가능한 사용자 아이디"
        );
        return ResponseEntity.ok(response);
    }

    // 로그아웃
//    void logout();

    // 회원 정보 단건 조회
    // 에러 처리 필요
    @GetMapping("/{userid}")
    public ResponseEntity<ResponseHandler<UserDTO>> getUser(@PathVariable Long userId){
        try {
            UserDTO user = userService.getUser(userId);

            ResponseHandler<UserDTO> response = new ResponseHandler<>(
                    user,
                    HttpStatus.OK.value(),   // 200
                    "사용자 조회 완료"
            );

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
//        catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return null;
    }

    // 회원 정보 수정
    @PutMapping("/{userid}")
    public ResponseEntity<ResponseHandler<UserDTO>> updateUser(@PathVariable Long userid, @RequestBody UserFormDTO dto){
        try {
            UserDTO updatedUser = userService.updateUser(userid, dto);
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
}
