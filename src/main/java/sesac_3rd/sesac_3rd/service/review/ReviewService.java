package sesac_3rd.sesac_3rd.service.review;

import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.review.ReviewDTO;
import sesac_3rd.sesac_3rd.entity.Review;

import java.util.List;

@Service
public interface ReviewService {

    // 리뷰 작성
    public Review createReview(ReviewDTO reviewDTO);

    // 장소별 리뷰 목록 조회
    List<Review> getAllReviewByPlaceId(Long placeId);

    // 리뷰 단건 조회
    ReviewDTO getReviewById(Long reviewId);

    // 리뷰 수정

    // 리뷰 삭제


}
