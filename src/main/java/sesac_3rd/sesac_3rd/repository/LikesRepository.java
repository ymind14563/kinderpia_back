package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sesac_3rd.sesac_3rd.entity.Likes;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    // 리뷰 좋아요
    List<Likes> findByReview_ReviewIdAndUser_UserId(Long reviewId, Long userId);

}
