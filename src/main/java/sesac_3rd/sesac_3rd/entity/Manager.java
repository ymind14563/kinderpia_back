package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Manager")
@Entity
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id", nullable = false)
    private Long managerId;  // 관리자아이디 (PK)

    @Column(name = "manager_login_id", nullable = false, length = 20)
    private String managerLoginId;  // 관리자로그인아이디

    @Column(name = "manager_pw", nullable = false, length = 100)
    private String managerPw;  // 관리자비밀번호

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;  // 닉네임
}
