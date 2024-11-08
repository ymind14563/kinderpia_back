package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.review.ReviewUserDTO(" +
            "r, " +
            "u.id AS writer, " +
            "u.nickname, " +
            "u.profileImg, " +
            "u.isBlacklist, " +
            "COUNT(l) AS likeCount, " +
            "CASE WHEN l.user.id = :userId THEN true ELSE false END AS isLikedByUser ) " +
            "FROM Review r " +
            "JOIN r.user u " +
            "LEFT JOIN Likes l ON l.review.id = r.id AND l.user.id = :userId " +
            "WHERE r.place.id = :placeId AND r.isDeleted = false " +
            "GROUP BY r.id, u.id " +
            "ORDER BY r.createdAt DESC")
    Page<ReviewUserDTO> findByPlace_PlaceId(@Param("placeId") Long placeId, @Param("userId") Long userId, Pageable pageable);

    // 로그인 유저 리뷰별 좋아요 여부
//   @Query("select round(avg(r.star)) from Review r where place_id=:placeId and is_deleted=false")


    // 장소 별 평균 별점 조회
    @Query(value = "select round(avg(r.star)) from review r where place_id=:placeId and is_deleted=false", nativeQuery = true)
    Integer getAverageStar(@Param("placeId") Long placeId);

    // 장소 별 리뷰 개수 조회
    @Query(value = "select count(r.place_id) from review r where place_id=:placeId and is_deleted=false ", nativeQuery = true)
    Integer getReviewCount(@Param("placeId") Long placeId);

    // 리뷰 작성
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.place.id = :placeId AND isDeleted = false")
    List<Review> findByUserIdAndPlaceId(@Param("userId") Long userId, @Param("placeId") Long placeId);
}