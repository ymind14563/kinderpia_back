package sesac_3rd.sesac_3rd.dto.review;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewFormDTO {
    // 리뷰 생성, 리뷰 수정
    private Long reviewId;
    private Long placeId;
    private Long userId;
    private int star;
    private String reviewContent;
}
