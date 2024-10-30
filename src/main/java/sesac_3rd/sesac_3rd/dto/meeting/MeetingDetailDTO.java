package sesac_3rd.sesac_3rd.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 모임 상세페이지 조회
public class MeetingDetailDTO {
    private Long meetingId;
    private String meetingCategory; // 카테고리명
    private String meetingTitle; // 모임명
    private String nickname; // 글쓴이
    private int capacity; // 참가인원
    private int totalCapacity; // 총원 (최대 99)
    private String meetingContent; // 모임내용
    private String meetingLocation; // 모임장소(주소)
    private boolean isAuthType; // 인증여부 (기본값 FALSE)
    private Long userId;
    private String placeName; // 장소명
    private String location; // 지역구
    private LocalDateTime meetingTime; // 모임일시
    private MeetingStatus meetingStatus; // 모임상태 (기본값 "ONGOING")
    private LocalDateTime createdAt; // 생성일자
}
