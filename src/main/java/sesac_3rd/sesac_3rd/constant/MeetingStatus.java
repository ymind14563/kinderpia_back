package sesac_3rd.sesac_3rd.constant;

// 모임상태
public enum MeetingStatus {
    ONGOING,   // 모집중
    COMPLETED,   // 모집완료 (인원마감, 모임장이 임의로 마감)
    END,   // 모임종료 (시간이 지나서 마감)
    DELETED   // 모임삭제 (모임장이 삭제, 관리자가 삭제)
}