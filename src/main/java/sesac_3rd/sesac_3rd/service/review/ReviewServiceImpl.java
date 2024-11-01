package sesac_3rd.sesac_3rd.service.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.review.ReviewDTO;
import sesac_3rd.sesac_3rd.dto.review.ReviewFormDTO;
import sesac_3rd.sesac_3rd.entity.Review;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.repository.ReviewRepository;

import java.util.List;
import static sesac_3rd.sesac_3rd.mapper.review.ReviewMapper.convertToDTO;
import sesac_3rd.sesac_3rd.mapper.review.ReviewMapper;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService{
    // 메서드 구현
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    // 장소별 리뷰 목록 조회
    @Override
    public List<Review> getAllReviewByPlaceId(Long placeId){
        List<Review> reviews = reviewRepository.findByPlace_PlaceId(placeId);
            System.out.println("reviews = " + reviews);
            log.info("reviews" + reviews);

        return reviews;
    }

    // 리뷰 단건 조회
    @Override
    public ReviewDTO getReviewById(Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new CustomException(ExceptionStatus.REVIEWID_NOT_FOUND));
        return convertToDTO(review);
    }

    // 리뷰 작성
    @Override
    public Review createReview(ReviewFormDTO reviewformDTO, User user) {
        Review review = reviewMapper.convertToEntity(reviewformDTO, user);
        return reviewRepository.save(review);
    }

    // 리뷰 수정
    @Override
    public ReviewFormDTO updateReview(Long reviewId, ReviewFormDTO reviewformDTO){
        try {
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(()->new CustomException(ExceptionStatus.REVIEWID_NOT_FOUND));
            review.setStar(reviewformDTO.getStar());
            review.setReviewContent(reviewformDTO.getReviewContent());
            reviewRepository.save(review);
            return reviewformDTO;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 리뷰 삭제
    @Override
    public boolean deleteReview(Long reviewId){
        try{
            reviewRepository.deleteById(reviewId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
