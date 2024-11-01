package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.dto.review.ReviewDTO;
import sesac_3rd.sesac_3rd.entity.Review;
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
    private ResponseEntity<ResponseHandler<List<Review>>> getAllReviewByPlaceId(@PathVariable("placeId") Long placeId){
        try{
            List<Review> reviewDTO = reviewService.getAllReviewByPlaceId(placeId);
            System.out.println("reviews" + reviewDTO);
            ResponseHandler<List<Review>> response = new ResponseHandler<>(
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
    private Review createReview(@RequestBody ReviewDTO dto){
        try{
             return reviewService.createReview(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
