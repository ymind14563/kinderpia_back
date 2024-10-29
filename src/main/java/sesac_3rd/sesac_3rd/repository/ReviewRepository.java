package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac_3rd.sesac_3rd.dto.review.ReviewDTO;
import sesac_3rd.sesac_3rd.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 리뷰 목록 조회
    List<Review> findAllById(Long placeId);
}
