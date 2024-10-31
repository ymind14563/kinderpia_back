package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @GetMapping("/{id}")
    private ResponseEntity<ResponseHandler<List<Review>>> getAllReviewByPlaceId(@PathVariable("id") Long placeId){
        try{
            List<Review> reviewDTO = reviewService.getAllReviewByPlaceId(placeId);
            ResponseHandler<List<Review>> response = new ResponseHandler<>(
                    reviewDTO,
                    HttpStatus.OK.value(),
                    "리뷰 목록 조회 완료"
            );
          return ResponseEntity.ok(response);  
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
