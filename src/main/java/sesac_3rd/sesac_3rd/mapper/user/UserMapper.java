package sesac_3rd.sesac_3rd.mapper.user;

import sesac_3rd.sesac_3rd.dto.user.*;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.entity.Review;
import sesac_3rd.sesac_3rd.entity.User;

import java.time.LocalDateTime;

public class UserMapper {
    // entity to dto
    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNum(user.getPhoneNum())
                .profileImg(user.getProfileImg())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // entity to formdto
    public static UserFormDTO toUserFormDTO(User user) {
        return UserFormDTO.builder()
                .nickname(user.getNickname())
                .phoneNum(user.getPhoneNum())
                .profileImg(user.getProfileImg())
                .build();
    }

    // formdto to entity for signup
    public static User toUserEntityForSignup(UserFormDTO formDTO, String encodedPw) {
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


    // entity to responsedto for return signup
    public static UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phoneNum(user.getPhoneNum())
                .profileImg(user.getProfileImg())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // entity to responsedto for return approval list
    public static UserResponseDTO toAppResponseDTO(User user){
        return UserResponseDTO.builder()
                .profileImg(user.getProfileImg())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .build();
    }

    // entity to loginformdto for return login success
    public static LoginFormDTO toLoginFormDTO(User user) {
        return LoginFormDTO.builder()
                .loginId(user.getLoginId())
                .userId(user.getUserId())
                .build();
    }


    // meeting entity to usermeetinglistDTO
    public static UserMeetingListDTO toUserMeetingListDTO(Meeting meeting) {
        return UserMeetingListDTO.builder()
                .meetingId(meeting.getMeetingId())
                .meetingTitle(meeting.getMeetingTitle())
                .meetingStatus(meeting.getMeetingStatus())
                .meetingTime(meeting.getMeetingTime())
                .meetingCtgName(meeting.getMeetingCategory().getMeetingCtgName())
                .meetingLocation(meeting.getMeetingLocation())
                .capacity(meeting.getCapacity())
                .totalCapacity(meeting.getTotalCapacity())
                .createdAt(meeting.getCreatedAt())
                .nickname(meeting.getUser().getNickname())
                .profileImg(meeting.getUser().getProfileImg())
                .build();
    }

    // meeting entity to usermeetinglistDTO for meetingtime
    public static UserMeetingListDTO toUserMeetingListTimeDTO(Meeting meeting) {
        return UserMeetingListDTO.builder()
                .meetingTitle(meeting.getMeetingTitle())
                .meetingTime(meeting.getMeetingTime())
                .build();
    }
}
