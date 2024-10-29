package sesac_3rd.sesac_3rd.mapper.user;

import sesac_3rd.sesac_3rd.dto.user.LoginFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.dto.user.UserFormDTO;
import sesac_3rd.sesac_3rd.dto.user.UserResponseDTO;
import sesac_3rd.sesac_3rd.entity.User;

import java.time.LocalDateTime;

public class UserMapper {
    // entity to dto
    public static UserDTO toUserDTO(User user){
        return UserDTO.builder()
//                .userPw(user.getUserPw())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNum(user.getPhoneNum())
                .profileImg(user.getProfileImg())
                .build();
    }

    // entity to formdto
    public static UserFormDTO toUserFormDTO(User user){
        return UserFormDTO.builder()
                .nickname(user.getNickname())
                .phoneNum(user.getPhoneNum())
                .profileImg(user.getProfileImg())
                .build();
    }

    // formdto to entity for signup
    public static User toUserEntityForSignup(UserFormDTO formDTO, String encodedPw){
        return User.builder()
                .userPw(encodedPw)
                .loginId(formDTO.getLoginId())
                .nickname(formDTO.getNickname())
                .email(formDTO.getEmail())
                .phoneNum(formDTO.getPhoneNum())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // formdto to entity for update user
    public static User toUserEntityForUpdate(UserFormDTO formDTO){
        return User.builder()
                .userPw(formDTO.getUserPw())
                .nickname(formDTO.getNickname())
                .phoneNum(formDTO.getPhoneNum())
                // 프로필 이미지는 dto에서 값 넘어오고 그 값이 비어있지 않다면 entity에 넣기
                .profileImg(formDTO.getProfileImg() != null && !formDTO.getProfileImg().isEmpty() ? formDTO.getProfileImg() : null)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // entity to responsedto for return signup
    public static UserResponseDTO toResponseDTO(User user){
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phoneNum(user.getPhoneNum())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // entity to loginformdto for return login success
    public static LoginFormDTO toLoginFormDTO(String token, User user){
        return LoginFormDTO.builder()
                .loginId(user.getLoginId())
                .userId(user.getUserId())
                .token(token)
                .build();
    }
}
