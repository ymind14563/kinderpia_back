package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.entity.*;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    // reporter + chatMessageId 로 요청을 보냈을 때, 이미 DB에 reporter + review 가 있는 상태인 경우 false 가 나와야 하는데,
    // reporter + chatMessage = false
    // reporter + review = true
    // reporter + meeting = false 가 나오므로
    // existsByReporterAndChatMessageOrReviewOrMeeting 이 값의 경우 falseOrtrueOrfalse 로 결과가 true 가 나와서 문제 발생
//    boolean existsByReporterAndChatMessageOrReviewOrMeeting(User reporter, ChatMessage chatMessage, Review review, Meeting meeting);


    // 위 문제 사항으로 각각 개별적으로 분리하여 조회
    boolean existsByReporterAndChatMessage(User reporter, ChatMessage chatMessage);
    boolean existsByReporterAndReview(User reporter, Review review);
    boolean existsByReporterAndMeeting(User reporter, Meeting meeting);

    long countByReported(User reported);

    Page<Report> findAllByChatMessageNotNull(Pageable pageable);

    Page<Report> findAllByReviewNotNull(Pageable pageable);

    Page<Report> findAllByMeetingNotNull(Pageable pageable);

    // 모임상세 접근시 사용자 상태 조회(신고여부)
    boolean existsByMeeting_MeetingIdAndReporter_UserId(Long meetingId, Long userId);

    // 총 신고 수
    @Query("SELECT COUNT(r.reportId) FROM Report r")
    long countById();
}
