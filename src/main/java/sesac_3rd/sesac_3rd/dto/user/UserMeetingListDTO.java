package sesac_3rd.sesac_3rd.dto.user;

import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 사용자 모임 목록 조회용
public class UserMeetingListDTO {
    private Long meetingId;  // 모임아이디
    private String meetingTitle;   // 모임명
    private MeetingStatus meetingStatus;   // 모임상태
    private LocalDateTime meetingTime;   // 모임일시
    private String meetingCtgName;  // 모임카테고리명
    private String meetingLocation;  // 모임장소
    private int capacity;  // 참가인원
    private int totalCapacity;  // 총원 (최대 99)
    private LocalDateTime createdAt;
    // 모임장 정보
    private String nickname;   // 닉네임
    private String profileImg;   // 프로필이미지
    private boolean isLeader;   // 모임장여부

}
