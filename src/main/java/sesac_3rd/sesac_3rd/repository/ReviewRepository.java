package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.dto.review.ReviewDTO;
import sesac_3rd.sesac_3rd.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 장소 별 리뷰 목록 조회
    @Query(value = "select * from Review where place_id= :placeId and is_deleted=false", nativeQuery = true)
    List<Review> findByPlace_PlaceId(@Param("placeId") Long placeId);

    // 장소 별 평균 별점 조회
    @Query(value = "select round(avg(r.star)) from Review r where place_id=:placeId", nativeQuery = true)
    Integer getAverageStar(@Param("placeId") Long placeId);
}
