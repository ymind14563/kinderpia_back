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
// 모임 목록 조회(검색)
public class MeetingDTO {
    private Long meetingId;
    private String nickname; // 모임장 닉네임
    private String profileImg; // 포로필 이미지 (유저)
    private int totalCapacity; // 총원 (최대 99)
    private String district; // 지역구
    private String detailAddress; // 상세주소
//    private String placeName; // 장소명
//    private String location; // 지역구
    private String meetingCategory; // 카테고리명
    private String meetingTitle; // 모임명
    private String meetingLocation; // 모임장소(주소)
    private LocalDateTime meetingTime; // 모임일시
    private int capacity; // 참가인원
    private MeetingStatus meetingStatus; // 모임상태
    private LocalDateTime createdAt; // 생성일자
}
