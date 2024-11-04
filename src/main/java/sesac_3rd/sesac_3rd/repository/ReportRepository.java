package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.entity.*;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByReporterAndChatMessageOrReviewOrMeeting(User reporter, ChatMessage chatMessage, Review review, Meeting meeting);

    long countByReported(User reported);

    Page<Report> findAllByChatMessageNotNull(Pageable pageable);

    Page<Report> findAllByReviewNotNull(Pageable pageable);

    Page<Report> findAllByMeetingNotNull(Pageable pageable);

}
