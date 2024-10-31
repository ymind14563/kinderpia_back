package sesac_3rd.sesac_3rd.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// 사용자 리뷰 목록 조회용
public class UserReviewDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserReviewListDTO{
        private Long reviewId;   // 리뷰아이디
        private String reviewContent;   // 리뷰내용
        private int star;   // 별점
        // 장소 정보도 포함
        private UserReviewDTO.PlaceInfoDTO place;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    // 장소 정보
    public static class PlaceInfoDTO {
        private Long placeId;
        private String placeName;
    }
}
