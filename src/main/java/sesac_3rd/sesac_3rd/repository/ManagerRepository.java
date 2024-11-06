package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sesac_3rd.sesac_3rd.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    // 관리자 로그인
    @Query("SELECT m FROM Manager m WHERE m.managerLoginId = :loginId AND m.managerPw = :password")
    Manager findByLoginIdAndPw(@Param("loginId") String loginId, @Param("password") String password);
}
