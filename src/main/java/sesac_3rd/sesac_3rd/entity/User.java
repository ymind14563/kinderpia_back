package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;
import sesac_3rd.sesac_3rd.dto.user.UserFormDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;   // 유저아이디

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Meeting> meetings;  // Meeting과 일대다 관계

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Review> reviews;  // Review와 일대다 관계

    @Column(name = "user_pw", nullable = false, length = 100)
    private String userPw;   // 유저비번

    @Column(name = "login_id", nullable = false, length = 20)
    private String loginId;   // 유저로그인아이디

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;   // 닉네임

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;    // 이메일

    @Column(name = "phone_num", nullable = false, unique = true, length = 30)
    private String phoneNum;   // 전화번호

    @Column(name = "profile_img", length = 300)
    private String profileImg;   // 프로필이미지

    @Column(name = "is_blacklist", nullable = false)
    private boolean isBlacklist = false;   // 블랙리스트여부

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;   // 탈퇴여부

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;   // 가입일자

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;   // 수정일자


}
