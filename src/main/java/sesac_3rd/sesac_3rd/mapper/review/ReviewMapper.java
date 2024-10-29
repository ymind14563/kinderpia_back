package sesac_3rd.sesac_3rd.mapper.review;

import sesac_3rd.sesac_3rd.dto.review.ReviewDTO;
import sesac_3rd.sesac_3rd.entity.Review;

public class ReviewMapper {

    // dto to entity
    public static Review convertToEntity(ReviewDTO dto) {
        return Review.builder()
                .reviewId(dto.getReviewId())
                .place(dto.getPlace())
                .user(dto.getUser())
                .star(dto.getStar())
                .reviewContent(dto.getReviewContent())
                .isDeleted(dto.isDeleted())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    // entity to dto
    public static ReviewDTO convertToDTO(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .place(review.getPlace())
                .user(review.getUser())
                .star(review.getStar())
                .reviewContent(review.getReviewContent())
                .isDeleted(review.isDeleted())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

}
