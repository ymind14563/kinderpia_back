package sesac_3rd.sesac_3rd.service.usermeeting;

import sesac_3rd.sesac_3rd.dto.usermeeting.UserMeetingJoinDTO;

public interface UserMeetingService {
    // 모임 참가
    void joinMeeting(UserMeetingJoinDTO userMeetingJoinDTO);
}
