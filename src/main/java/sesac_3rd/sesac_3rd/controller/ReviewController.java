package sesac_3rd.sesac_3rd.controller;

import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.config.security.TokenProvider;
import sesac_3rd.sesac_3rd.dto.review.*;
import sesac_3rd.sesac_3rd.entity.Likes;
import sesac_3rd.sesac_3rd.entity.Review;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.service.review.ReviewService;
import sesac_3rd.sesac_3rd.config.security.TokenProvider;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private TokenProvider tokenProvider;

    // 리뷰 목록 조회
    // localhost:8080/api/review?placeId=2&page=0&size=10
    @GetMapping
    private ResponseEntity<ResponseHandler<ReviewListDTO>> getAllReviewByPlaceId(
            @RequestParam(value = "placeId", required = false) Long placeId,
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page, // 시작 페이지
            @RequestParam(value = "size", defaultValue = "10") int size // 크기
    ) {
        try {
            if (placeId == null) {
                ResponseHandler<ReviewListDTO> response = new ResponseHandler<>(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        "placeId를 입력해 주세요."
                );
                return ResponseEntity.badRequest().body(response);
            }

            System.out.println("placeId : " + placeId);
            System.out.println("login userId : " + userId);
            System.out.println("page : " + page);
            System.out.println("size : " + size);
            ReviewListDTO reviewDTO = reviewService.getAllReviewByPlaceId(placeId, userId, page, size);
            System.out.println("reviews: " + reviewDTO);
            ResponseHandler<ReviewListDTO> response = new ResponseHandler<>(
                    reviewDTO,
                    HttpStatus.OK.value(), // 200
                    "리뷰 목록 조회 완료"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // 리뷰 단건 조회
    @GetMapping("/review/{reviewid}")
    private ResponseEntity<ResponseHandler<ReviewDTO>> getReviewById(@PathVariable(value = "reviewid") Long reviewId){
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
            System.out.println("userId >>> "+ userId);
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
    private ResponseEntity<ResponseHandler<ReviewFormDTO>> updateReview(
            @PathVariable(value = "id") Long reviewId,
            @AuthenticationPrincipal Long userId,
            @RequestBody ReviewFormDTO rfdto){
        try{
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId >> null ? "+ userId);
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId >>> "+ userId);

            ReviewFormDTO reviewFormDTO = reviewService.updateReview(reviewId, userId, rfdto);
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
    private ResponseEntity<ResponseHandler<Boolean>> deleteReview(
            @PathVariable(value = "id") Long reviewId,
            @AuthenticationPrincipal Long userId
    ){
        try{
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId >> null ? "+ userId);
                return ResponseHandler.unauthorizedResponse();
            }

            System.out.println("userId >>> "+ userId);
            Boolean result = reviewService.deleteReview(reviewId,userId);
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
    private ResponseEntity<ResponseHandler<Likes>> postLike(
            @PathVariable(value = "id") Long reviewId,
            @AuthenticationPrincipal Long userId){
        try {
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("로그인이 필요합니다.");
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId >>> "+ userId);
            Likes result = reviewService.postLike(reviewId, userId);
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
