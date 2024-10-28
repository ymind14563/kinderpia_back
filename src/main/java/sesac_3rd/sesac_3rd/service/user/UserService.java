package sesac_3rd.sesac_3rd.service.user;

import sesac_3rd.sesac_3rd.dto.user.LoginFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.dto.user.UserFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserResponseDTO;

public interface UserService {
    // 로그인
    boolean userLogin(LoginFormDTO dto);

    // 회원가입
    UserResponseDTO register(UserFormDTO dto);

    // 회원가입 - 닉네임 중복 검사
    void isNicknameDuplicate(String nickname);

    // 회원가입 - 아이디 중복 검사
    void isLoginIdDuplicate(String loginId);

    // 회원가입 - 이메일 중복 검사
    void isEmailDuplicate(String email);

    // 회원가입 - 전화번호 중복 검사
    void isPhonenumDuplicate(String phoneNum);

    // 로그아웃
    void logout();

    // 회원 정보 단건 조회
    UserDTO getUser(Long userId);

    // 회원 정보 수정
    UserDTO updateUser(Long userId, UserFormDTO dto);

    // 회원 탈퇴


    // 비밀번호 일치 확인(탈퇴시)
}
