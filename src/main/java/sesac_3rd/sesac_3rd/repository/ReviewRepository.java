package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.dto.review.ReviewDTO;
import sesac_3rd.sesac_3rd.dto.review.ReviewUserDTO;
import sesac_3rd.sesac_3rd.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 리뷰 아이디 조회
    Review findByReviewId(Long reviewId);

    // 장소 별 리뷰 목록 조회
//    @Query(value = "SELECT DISTINCT r.*, u.nickname, u.profile_img, u.is_blacklist FROM Review r JOIN User u ON r.user_id = u.user_id WHERE r.place_id = :placeId AND r.is_deleted = false ORDER BY r.created_at DESC", nativeQuery = true)
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.review.ReviewUserDTO(" +
            "r, " +
            "u.id, "+
            "u.nickname, " +
            "u.profileImg, " +
            "u.isBlacklist, " +
            "(SELECT COUNT(l) FROM Likes l WHERE l.review.id = r.id)) " +
            "FROM Review r " +
            "JOIN r.user u " +
            "WHERE r.place.id = :placeId AND r.isDeleted = false " +
            "ORDER BY r.createdAt DESC")
    List<ReviewUserDTO> findByPlace_PlaceId(@Param("placeId") Long placeId);

    // 장소 별 평균 별점 조회
    @Query(value = "select round(avg(r.star)) from Review r where place_id=:placeId and is_deleted=false", nativeQuery = true)
    Integer getAverageStar(@Param("placeId") Long placeId);

    // 리뷰 작성
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.place.id = :placeId")
    List<Review> findByUserIdAndPlaceId(@Param("userId") Long userId, @Param("placeId") Long placeId);
}