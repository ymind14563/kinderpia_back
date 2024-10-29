package sesac_3rd.sesac_3rd.dto.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 로그인용
public class LoginFormDTO {
    private Long userId;
    private String loginId;
    private String userPw;
    private String token;
}
