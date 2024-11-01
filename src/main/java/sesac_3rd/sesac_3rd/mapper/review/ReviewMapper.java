package sesac_3rd.sesac_3rd.mapper.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sesac_3rd.sesac_3rd.dto.review.ReviewDTO;
import sesac_3rd.sesac_3rd.dto.review.ReviewFormDTO;
import sesac_3rd.sesac_3rd.entity.*;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.repository.PlaceRepository;
import sesac_3rd.sesac_3rd.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ReviewMapper {

    @Autowired
    private static PlaceRepository placeRepository;

    @Autowired
    private static UserRepository userRepository;

    // dto to entity
    public static Review convertToEntity(ReviewFormDTO dto, User user) {
        Optional<Place> place = placeRepository.findById(dto.getPlace().getPlaceId());

        Review review = new Review();
        review.setPlace(dto.getPlace());
        review.setReviewContent(dto.getReviewContent());

        return review;
    }

    // entity to dto
    public static ReviewDTO convertToDTO(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .place(review.getPlace())
                .user(review.getUser())
                .reviewContent(review.getReviewContent())
                .isDeleted(review.isDeleted())
                .build();
    }
}
