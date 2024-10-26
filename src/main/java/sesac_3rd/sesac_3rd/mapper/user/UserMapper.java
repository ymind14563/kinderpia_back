package sesac_3rd.sesac_3rd.mapper.user;

import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.dto.user.UserFormDTO;
import sesac_3rd.sesac_3rd.entity.User;

public class UserMapper {
    // entity to dto
    public static UserDTO toUserDTO(User user){
        return UserDTO.builder()
                .userPw(user.getUserPw())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNum(user.getPhoneNum())
                .profileImg(user.getProfileImg())
                .build();
    }

    // formdto to entity
    public static User toUserEntity(UserFormDTO formDTO){
        return User.builder()
                .userPw(formDTO.getUserPw())
                .loginId(formDTO.getLoginId())
                .nickname(formDTO.getNickname())
                .email(formDTO.getEmail())
                .phoneNum(formDTO.getPhoneNum())
                .profileImg(formDTO.getProfileImg())
                .build();
    }
}
