package sesac_3rd.sesac_3rd.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.user.LoginFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.dto.user.UserFormDTO;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.mapper.user.UserMapper;
import sesac_3rd.sesac_3rd.repository.UserRepository;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 로그인
    @Override
    public boolean userLogin(LoginFormDTO dto){
        return false;
    }

    // 회원가입
    // 가입일자, 수정일자 현재 시간으로 삽입
    @Override
    public void register(UserFormDTO dto){
        validateUserDetails(dto);   // 커스텀 에러 처리 필요
        // 비번 인코딩 후 insert
        String encodedPw = passwordEncoder.encode(dto.getUserPw());
        User newUser = UserMapper.toUserEntityForSignup(dto, encodedPw);
        userRepository.save(newUser);
        // 리턴값 void로 할지 결정 필요
    }

    // 회원가입 - 닉네임 중복 검사
    @Override
    public boolean isNicknameDuplicate(String nickname){
        boolean isDuplicated = userRepository.existsByNickname(nickname);
        return isDuplicated;
    }

    // 회원가입 - 아이디 중복 검사
    @Override
    public boolean isLoginIdDuplicate(String loginId){
        boolean isDuplicated = userRepository.existsByLoginId(loginId);
        return isDuplicated;
    }

    // 로그아웃
    @Override
    public void logout(){

    }

    // 회원 정보 단건 조회
    // 커스텀 예외 처리 필요
    @Override
    public UserDTO getUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException());
        UserDTO dto = UserMapper.toUserDTO(user);
        return dto;
    }

    // 회원 정보 수정
    // 수정일자 현재 시간으로 삽입
    @Override
    public UserFormDTO updateUser(UserFormDTO dto){
        return new UserFormDTO();
    }


    // 회원가입 시 모든 예외
    // 커스텀 에러 뭐할지 정하기
    private void validateUserDetails(UserFormDTO formDTO) {
        // 유효성 검사
        validateNickname(formDTO.getNickname());
        validateLoginId(formDTO.getLoginId());
        validatePassword(formDTO.getUserPw());
        validateEmail(formDTO.getEmail());
        validatePhoneNumber(formDTO.getPhoneNum());

        // 유니크 제약 조건 검사
        if (userRepository.existsByEmail(formDTO.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByPhoneNum(formDTO.getPhoneNum())) {
            throw new IllegalArgumentException("이미 사용 중인 전화번호입니다.");
        }
    }

    private void validateNickname(String nickname) {
        if (nickname.length() < 2 || nickname.length() > 15 || !Pattern.matches("^[가-힣a-zA-Z0-9]+$", nickname)) {
            throw new IllegalArgumentException("닉네임은 2글자 이상 15글자 이하이며, 한글, 영어, 숫자만 가능합니다.");
        }
    }

    private void validateLoginId(String loginId) {
        if (loginId.length() < 6 || loginId.length() > 12 || !Pattern.matches("^[a-z0-9]+$", loginId)) {
            throw new IllegalArgumentException("아이디는 6글자 이상 12글자 이하이며, 영어 소문자와 숫자만 가능합니다.");
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8 || password.length() > 16 || !Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$", password)) {
            throw new IllegalArgumentException("비밀번호는 8글자 이상 16글자 이하이며, 영어와 숫자를 포함해야 합니다.");
        }
    }

    private void validateEmail(String email) {
        if (!email.contains("@") || email.indexOf('.') < email.indexOf('@') + 2) {
            throw new IllegalArgumentException("유효한 이메일 형식이 아닙니다.");
        }
    }

    private void validatePhoneNumber(String phoneNum) {
        if (!Pattern.matches("^\\d{10,11}$", phoneNum)) {
            throw new IllegalArgumentException("전화번호는 10~11자리 숫자여야 합니다.");
        }
    }
}
