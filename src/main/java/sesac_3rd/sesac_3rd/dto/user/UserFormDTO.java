package sesac_3rd.sesac_3rd.dto.user;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 회원가입용, 회원수정용
public class UserFormDTO {
    private String userPw;   // 유저비번
    private String loginId;   // 유저로그인아이디
    private String nickname;   // 닉네임
    private String email;    // 이메일
    private String phoneNum;   // 전화번호
    private String profileImg;   // 프로필이미지
    private LocalDateTime createdAt;   // 가입일자
    private LocalDateTime updatedAt;
}
