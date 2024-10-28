package sesac_3rd.sesac_3rd.service.user;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sesac_3rd.sesac_3rd.dto.user.LoginFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.dto.user.UserFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserResponseDTO;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.mapper.user.UserMapper;
import sesac_3rd.sesac_3rd.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 로그인
    @Override
    public boolean userLogin(LoginFormDTO dto) {
        return false;
    }

    // 회원가입
    @Override
    public UserResponseDTO register(UserFormDTO dto) {
        log.info("register user");
        validateUserDetails(dto);   // 커스텀 에러 처리 필요
        // 비번 인코딩 후 insert
        String encodedPw = passwordEncoder.encode(dto.getUserPw());
        User newUser = UserMapper.toUserEntityForSignup(dto, encodedPw);
        User savedUser = userRepository.save(newUser);

        return UserMapper.toResponseDTO(savedUser);
    }

    // 회원가입 - 닉네임 중복 검사
    @Override
    public boolean isNicknameDuplicate(String nickname) {
        log.info("check nickname duplicated -==-=-= {}", nickname);
        boolean isDuplicated = userRepository.existsByNickname(nickname);
        log.info("isDuplicated --- {}", isDuplicated);
        return isDuplicated;
    }

    // 회원가입 - 아이디 중복 검사
    @Override
    public boolean isLoginIdDuplicate(String loginId) {
        log.info("check loginid duplicated");
        boolean isDuplicated = userRepository.existsByLoginId(loginId);
        return isDuplicated;
    }

    // 로그아웃
    @Override
    public void logout() {

    }

    // 회원 정보 단건 조회
    // 커스텀 예외 처리 필요
    @Override
    public UserDTO getUser(Long userId) {
        log.info("get user : {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException());  //new CustomException(ErrorCode.USER_NOT_FOUND)
        UserDTO userDTO = UserMapper.toUserDTO(user);
        return userDTO;
    }

    // 회원 정보 수정
    // 닉네임, 비번, 전번, 프로필 이미지 수정 가능
    // 수정일자 현재 시간으로 삽입
    // 로직 프론트랑 얘기해서 바꿔야 될수도
    @Override
    public UserDTO updateUser(Long userId, UserFormDTO dto) {
        log.info("update user : {}", userId);
        // 1. 사용자 조회
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException());  //new CustomException(ErrorCode.USER_NOT_FOUND)

        // 2. 변경하려는 필드 중복 검사(닉네임, 전화번호)
        validateDuplicateFieldsForUpdate(userId, dto);

        // 3. 각 필드 유효성 검사
        // 닉네임 수정
        if (StringUtils.hasText(dto.getNickname())) {
            validateNickname(dto.getNickname());
            existingUser.setNickname(dto.getNickname());
        }

        // 비번 수정
        if (StringUtils.hasText(dto.getUserPw())) {
            validatePassword(dto.getUserPw());
            String encodedPw = passwordEncoder.encode(dto.getUserPw());
            existingUser.setUserPw(encodedPw);
        }

        // 전화번호 수정
        if (StringUtils.hasText(dto.getPhoneNum())) {
            validatePhoneNumber(dto.getPhoneNum());
            existingUser.setPhoneNum(dto.getPhoneNum());
        }

        // 프로필 이미지 수정
        if (StringUtils.hasText(dto.getProfileImg())) {
            // 이미지 관련 S3 추가, String말고 MultipartFile 로 바꿔야 할수도
//            existingUser.setProfileImg(dto.getProfileImg());
        }
        // 수정 날짜 업데이트
        existingUser.setUpdatedAt(LocalDateTime.now());
        // 수정된 formdto를 entity로 변경
//        User updateUser = UserMapper.toUserEntityForUpdate(dto);

        User updatedUser = userRepository.save(existingUser);
//        UserDTO userDTO = UserMapper.toUserDTO(updatedUser);
        return UserMapper.toUserDTO(updatedUser);
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

        // 중복 검사
        validateDuplicateFields(formDTO);
    }
    // User Registration
//    INVALID_NICKNAME_FORMAT(400, "U001", "닉네임은 2글자 이상 15글자 이하이며, 한글, 영어, 숫자만 가능합니다."),
//    INVALID_LOGIN_ID_FORMAT(400, "U002", "아이디는 6글10자 이상 12글자 이하이며, 영어 소문자와 숫자만 가능합니다."),
//    INVALID_PASSWORD_FORMAT(400, "U003", "비밀번호는 8글자 이상 16글자 이하이며, 영어와 숫자를 포함해야 합니다."),
//    INVALID_EMAIL_FORMAT(400, "U004", "유효한 이메일 형식이 아닙니다."),
//    INVALID_PHONE_FORMAT(400, "U005", "전화번호는 10~11자리 숫자여야 합니다."),
//    DUPLICATE_EMAIL(409, "U006", "이미 사용 중인 이메일입니다."),
//    DUPLICATE_PHONE(409, "U007", "이미 사용 중인 전화번호입니다."),
//    DUPLICATE_LOGIN_ID(409, "U008", "이미 사용 중인 아이디입니다.");
    //    DUPLICATE_NICKNAME(409, "U008", "이미 사용 중인 닉네임입니다.");

    // 회원가입 시 중복 검사
    private void validateDuplicateFields(UserFormDTO dto) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(dto.getEmail())) {
//            throw new CustomException(ErrorCode.DUPLICATE_EMAIL); 이미 사용 중인 이메일입니다.
        }

        // 전화번호 중복 검사
        if (userRepository.existsByPhoneNum(dto.getPhoneNum())) {
//            throw new CustomException(ErrorCode.DUPLICATE_PHONE); 이미 사용 중인 전화번호입니다.
        }

        // 로그인 ID 중복 검사
//        if (userRepository.existsByLoginId(dto.getLoginId())) {
//            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID); 이미 사용 중인 아이디입니다.
//        }
    }

    // 닉네임 유효성 검사
    private void validateNickname(String nickname) {
        if (nickname == null || !Pattern.matches("^[가-힣a-zA-Z0-9]{2,15}$", nickname)) {
//            throw new CustomException(ErrorCode.INVALID_NICKNAME_FORMAT); 닉네임은 2글자 이상 15글자 이하이며, 한글, 영어, 숫자만 가능합니다.
        }
    }

    // 아이디 유효성 검사
    private void validateLoginId(String loginId) {
        if (loginId == null || !Pattern.matches("^[a-z0-9]{6,12}$", loginId)) {
//            throw new CustomException(ErrorCode.INVALID_LOGIN_ID_FORMAT); 아이디는 6글자 이상 12글자 이하이며, 영어 소문자와 숫자만 가능합니다.
        }
    }

    // 비번 유효성 검사
    private void validatePassword(String password) {
        if (password == null ||
                !Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d]{8,16}$", password)) {
//            throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT); 비밀번호는 8글자 이상 16글자 이하이며, 영어와 숫자를 포함해야 합니다.
        }
    }

    // 이메일 유효성 검사
    private void validateEmail(String email) {
        if (email == null ||
                !Pattern.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", email)) {
//            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT); 유효한 이메일 형식이 아닙니다.
        }
    }

    // 전화번호 유효성 검사
    private void validatePhoneNumber(String phoneNum) {
        if (phoneNum == null || !Pattern.matches("^\\d{10,11}$", phoneNum)) {
//            throw new CustomException(ErrorCode.INVALID_PHONE_FORMAT); 전화번호는 10~11자리 숫자여야 합니다.
        }
    }

    // 수정시 중복 검사(자신의 현재 값은 제외)
    private void validateDuplicateFieldsForUpdate(Long userId, UserFormDTO dto) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException());

        // 전화번호 중복 검사(현재 사용자 제외)
        if (StringUtils.hasText(dto.getPhoneNum()) && !dto.getPhoneNum().equals(currentUser.getPhoneNum())) {
            if (userRepository.existsByPhoneNum(dto.getPhoneNum())) {
//                throw new CustomException(ErrorCode.DUPLICATE_PHONE); 이미 사용 중인 전화번호입니다.
            }
        }

        // 닉네임 중복 검사(현재 사용자 제외)
        if (StringUtils.hasText(dto.getNickname()) && !dto.getNickname().equals(currentUser.getNickname())) {
            if (userRepository.existsByNickname(dto.getNickname())) {
//                throw new CustomException(ErrorCode.DUPLICATE_NICKNAME); 이미 사용 중인 닉네임입니다.
            }
        }

    }
}
