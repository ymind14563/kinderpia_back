package sesac_3rd.sesac_3rd.service.user;

import sesac_3rd.sesac_3rd.dto.user.*;

public interface UserService {
    // 로그인
    LoginResponse userLogin(String loginId, String userPw);

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
    void deleteUser(Long userId);

    // 비밀번호 일치 확인(회원 수정, 탈퇴시)
    void checkUserPw(Long userId, String userPw);

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장인지 아닌지 필터링 요청 api 분리) - 페이지네이션

    // 사용자 모임 일정 목록 조회(사용자가 모임장이거나 속해있는 모임, 삭제된 모임 제외)

    // 사용자 리뷰 목록 조회(장소 정보까지 같이)
}
