package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Review")
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId;  // 리뷰아이디 (PK)

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)  // 장소아이디 (외래 키)
    private Place place;  // Place 엔티티와 다대일 관계

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // 유저아이디 (외래 키)
    private User user;  // User 엔티티와 다대일 관계

    @Column(name = "star", nullable = false)
    private int star = 0;  // 별점 (기본값 0)

    @Column(name = "review_content", nullable = false, length = 500)
    private String reviewContent;  // 리뷰내용

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;  // 삭제여부 (기본값 FALSE)

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;  // 생성일자

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;  // 수정일자
}
