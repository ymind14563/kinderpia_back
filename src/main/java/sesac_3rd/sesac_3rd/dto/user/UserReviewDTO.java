package sesac_3rd.sesac_3rd.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 사용자 리뷰 목록 조회용
public class UserReviewDTO {
    private Long reviewId;   // 리뷰아이디
    private String reviewContent;   // 리뷰내용
    private int star;   // 별점
    private LocalDateTime createdAt;
    private long likeCount;
    // 장소 정보도 포함
    private Long placeId;
    private String placeName;

}
