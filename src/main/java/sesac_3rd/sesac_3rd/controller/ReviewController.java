package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.dto.review.*;
import sesac_3rd.sesac_3rd.entity.Likes;
import sesac_3rd.sesac_3rd.entity.Review;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.service.review.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // 리뷰 목록 조회
    @GetMapping("/{placeId}")
    private ResponseEntity<ResponseHandler<ReviewListDTO>> getAllReviewByPlaceId(@PathVariable("placeId") Long placeId){
        try{
            ReviewListDTO reviewDTO = reviewService.getAllReviewByPlaceId(placeId);
            System.out.println("reviews" + reviewDTO);
            ResponseHandler<ReviewListDTO> response = new ResponseHandler<>(
                    reviewDTO,
                    HttpStatus.OK.value(), //200
                    "리뷰 목록 조회 완료"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 리뷰 단건 조회
    @GetMapping("/review/{reviewid}")
    private ResponseEntity<ResponseHandler<ReviewDTO>> getReviewById(@PathVariable("reviewid") Long reviewId){
        try{
            ReviewDTO reviewDTO = reviewService.getReviewById(reviewId);
            ResponseHandler<ReviewDTO> response = new ResponseHandler<>(
                    reviewDTO,
                    HttpStatus.OK.value(), //200
                    "리뷰 단건 조회 완료"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 리뷰 작성
    @PostMapping
    private ResponseEntity<ResponseHandler<Review>> createReview(
            @AuthenticationPrincipal Long userId,
            @RequestBody ReviewFormDTO dto
    ){
        try{
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId >> null ? "+ userId);
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId "+ userId);
            Review review = reviewService.createReview(dto, userId);
            ResponseHandler<Review> response = new ResponseHandler<>(
                    review,
                    HttpStatus.OK.value(), //200
                    "리뷰 작성 완료"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // 리뷰 수정
    @PutMapping("/update/{id}")
    private ResponseEntity<ResponseHandler<ReviewFormDTO>> updateReview(@PathVariable("id") Long reviewId, @RequestBody ReviewFormDTO rfdto){
        try{
            ReviewFormDTO reviewFormDTO = reviewService.updateReview(reviewId, rfdto);
            ResponseHandler<ReviewFormDTO> response = new ResponseHandler<>(
                    reviewFormDTO,
                    HttpStatus.OK.value(), //200
                    "리뷰 수정 완료"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // 리뷰 삭제
    @DeleteMapping("/delete/{id}")
    private ResponseEntity<ResponseHandler<Boolean>> deleteReview(@PathVariable("id") Long reviewId, @RequestBody ReviewFormDTO dto){
        try{

            Boolean result = reviewService.deleteReview(reviewId);
            ResponseHandler<Boolean> response = new ResponseHandler<>(
                    result, //true
                    HttpStatus.OK.value(), // 200
                    "리뷰 삭제 완료"
            );
            return ResponseEntity.ok(response);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    // 리뷰 좋아요
    @PostMapping("/likes/{id}")
    private ResponseEntity<ResponseHandler<Likes>> postLike(@PathVariable("id") Long reviewId, @RequestBody LikesDTO likesDTO, User user, Review review){
        try {
            Likes result = reviewService.postLike(reviewId, likesDTO, user, review);
            ResponseHandler<Likes> response = new ResponseHandler<>(
                    result,
                    HttpStatus.OK.value(), //200
                    "좋아요 클릭"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}