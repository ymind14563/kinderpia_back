package sesac_3rd.sesac_3rd.service.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.review.*;
import sesac_3rd.sesac_3rd.entity.Likes;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.entity.Review;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.mapper.review.LikesMapper;
import sesac_3rd.sesac_3rd.repository.LikesRepository;
import sesac_3rd.sesac_3rd.repository.PlaceRepository;
import sesac_3rd.sesac_3rd.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static sesac_3rd.sesac_3rd.mapper.review.ReviewMapper.convertToDTO;
import sesac_3rd.sesac_3rd.mapper.review.ReviewMapper;
import sesac_3rd.sesac_3rd.repository.UserRepository;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService{
    // 메서드 구현
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private LikesMapper likesMapper;

    // 장소별 리뷰 목록 조회
    @Override
    public ReviewListDTO getAllReviewByPlaceId(Long placeId){
        List<ReviewUserDTO> reviews = reviewRepository.findByPlace_PlaceId(placeId);
        System.out.println("reviews = " + reviews);

        Integer avgStar = reviewRepository.getAverageStar(placeId);
        System.out.println("avgStar : " + avgStar);

        return new ReviewListDTO(reviews, avgStar);
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
        User checkUser = userRepository.findByUserId(reviewformDTO.getUserId());
        if(checkUser == null){
            throw new CustomException(ExceptionStatus.USER_NOT_FOUND);
        }
        Place checkPlace = placeRepository.findByPlaceId(reviewformDTO.getPlaceId());
        if(checkPlace == null){
            throw new CustomException(ExceptionStatus.PLACE_NOT_FOUND);
        }
        List<Review> checkReview = reviewRepository.findByUserIdAndPlaceId(reviewformDTO.getUserId(), reviewformDTO.getPlaceId());
        for (Review review : checkReview) {
            System.out.println("Review ID: " + review.getReviewId());
            System.out.println("Content: " + review.getReviewContent());
            System.out.println("Star: " + review.getStar());
            System.out.println("userId : " + review.getUser().getUserId());
            System.out.println("placeid : " + review.getPlace().getPlaceId());
        }
        if(!checkReview.isEmpty()){
            throw new CustomException(ExceptionStatus.REVIEW_ALREADY_WRITE);
        }
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
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(()->new CustomException(ExceptionStatus.REVIEWID_NOT_FOUND));
            review.setDeleted(true);
            reviewRepository.save(review);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 리뷰 좋아요
    public Likes postLike(Long reviewId, LikesDTO likesDTO, User user, Review review){
        try{
            List<Likes> checkLikes = likesRepository.findByReview_ReviewIdAndUser_UserId(reviewId, likesDTO.getUserId());
            if(!checkLikes.isEmpty()){
                Likes existLike = checkLikes.get(0);
                likesRepository.delete(existLike);
                return existLike;
            }else {
                Likes likes = likesMapper.convertToEntity(likesDTO, user, review);
                return likesRepository.save(likes);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
