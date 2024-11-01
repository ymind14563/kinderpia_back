package sesac_3rd.sesac_3rd.mapper.review;

import sesac_3rd.sesac_3rd.dto.review.ReviewDTO;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.entity.Review;

import java.time.LocalDateTime;

public class ReviewMapper {

    // dto to entity
    public static Review convertToEntity(ReviewDTO dto) {
        return Review.builder()
                .reviewId(dto.getReviewId())
//                .place(dto.getPlaceId())
                .user(dto.getUser())
                .star(dto.getStar())
                .reviewContent(dto.getReviewContent())
                .isDeleted(dto.isDeleted())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // entity to dto
    public static ReviewDTO convertToDTO(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .user(review.getUser())
                .star(review.getStar())
                .reviewContent(review.getReviewContent())
                .isDeleted(review.isDeleted())
                .build();
    }
}
