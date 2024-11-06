package sesac_3rd.sesac_3rd.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponseDTO {
    private Long userId;
    private String loginId;
    private String nickname;
    private String email;
    private String phoneNum;
    private String profileImg;
    private LocalDateTime createdAt;

    @Builder
    private UserResponseDTO(Long userId, String loginId, String nickname, String email, String phoneNum, String profileImg, LocalDateTime createdAt){
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.loginId = loginId;
        this.profileImg = profileImg;
        this.createdAt = createdAt;
    }
}
