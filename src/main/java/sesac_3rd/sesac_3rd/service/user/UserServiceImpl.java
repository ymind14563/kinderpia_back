package sesac_3rd.sesac_3rd.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.user.LoginFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.dto.user.UserFormDTO;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.mapper.user.UserMapper;
import sesac_3rd.sesac_3rd.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    // 로그인
    @Override
    public boolean userLogin(LoginFormDTO dto){
        return false;
    }

    // 회원가입
    // 가입일자, 수정일자 현재 시간으로 삽입
    @Override
    public void register(UserFormDTO dto){

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
}
