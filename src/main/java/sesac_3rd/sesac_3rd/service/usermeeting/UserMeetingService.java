package sesac_3rd.sesac_3rd.service.usermeeting;

import sesac_3rd.sesac_3rd.dto.usermeeting.UserMeetingJoinDTO;

public interface UserMeetingService {
    // 모임 참가
    void joinMeeting(Long meetingId, UserMeetingJoinDTO userMeetingJoinDTO);

    // 모임 탈퇴
    void exitMeeting(Long meetingId);

    // 모임 수락
    void isAccepted(Long meetingId, Long userId);
}
