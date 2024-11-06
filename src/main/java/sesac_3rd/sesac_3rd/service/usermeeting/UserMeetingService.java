package sesac_3rd.sesac_3rd.service.usermeeting;

import sesac_3rd.sesac_3rd.dto.usermeeting.UserMeetingJoinDTO;

public interface UserMeetingService {
    // 모임 참가
    void joinMeeting(Long userId, Long meetingId, UserMeetingJoinDTO userMeetingJoinDTO);

    // 모임 탈퇴
    Boolean exitMeeting(Long userId, Long meetingId);

    // 모임 수락
    Boolean isAccepted(Long userId, Long meetingId, Long joinUserId);

    // 모임 거절
    Boolean isRejection(Long userId, Long meetingId, Long joinUserId);
}
