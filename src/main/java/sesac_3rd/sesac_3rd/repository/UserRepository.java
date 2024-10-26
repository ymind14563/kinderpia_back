package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 아이디 중복 확인
    boolean existsByLoginId(String loginId);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);

    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 전화번호 중복 확인
    boolean existsByPhoneNum(String phoneNum);

}
