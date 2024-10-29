package sesac_3rd.sesac_3rd.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 모임 목록 (default - 최신순 정렬)
public class MeetingDTO {
    private Long meetingId;
    private Long placeId;
    private Long meetingCategory; // 카테고리
    private String meetingTitle; // 타이틀
    private int capacity; // 참가인원
    private String meetingLocation; // 모임장소
    private LocalDateTime meetingTime; // 모임일시
    private MeetingStatus meetingStatus; // 모임 상태
    private LocalDateTime createdAt; // 생성일자
}
