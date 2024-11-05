package sesac_3rd.sesac_3rd.mapper.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sesac_3rd.sesac_3rd.dto.review.LikesDTO;
import sesac_3rd.sesac_3rd.entity.Likes;
import sesac_3rd.sesac_3rd.entity.Review;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.repository.PlaceRepository;
import sesac_3rd.sesac_3rd.repository.ReviewRepository;
import sesac_3rd.sesac_3rd.repository.UserRepository;

@Component
public class LikesMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // dto to entity - 좋아요 눌렀을 시
    public Likes convertToEntity(Long reviewId, Long userId){
        User getUserId = userRepository.findByUserId(userId);
        if(getUserId == null){
            throw new CustomException(ExceptionStatus.USER_NOT_FOUND);
        }
        Review getreviewId = reviewRepository.findByReviewId(reviewId);
        if(getreviewId == null) {
            throw new CustomException(ExceptionStatus.REVIEWID_NOT_FOUND);
        }
        Likes likes = new Likes();
        likes.setReview(getreviewId);
        likes.setUser(getUserId);
        return likes;
    }


}
