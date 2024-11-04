package sesac_3rd.sesac_3rd.mapper.usermeeting;

import sesac_3rd.sesac_3rd.dto.usermeeting.UserMeetingJoinDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.entity.UserMeeting;

public class UserMeetingMapper {
    // [ 모임 참가 ] dto to entity
    public static UserMeeting toUserMeetingJoinEntity(UserMeetingJoinDTO userMeetingJoinDTO) {
        User user = User.builder().userId(userMeetingJoinDTO.getUserId()).build();
        Meeting meeting = Meeting.builder().meetingId(userMeetingJoinDTO.getMeetingId()).build();

        return UserMeeting.builder()
                .user(user)
                .meeting(meeting)
                .capacity(userMeetingJoinDTO.getCapacity())
                .isAccepted(userMeetingJoinDTO.isAccepted())
                .isBlocked(userMeetingJoinDTO.isBlocked())
                .isWithdraw(userMeetingJoinDTO.isWithdraw())
                .build();

    }
}
