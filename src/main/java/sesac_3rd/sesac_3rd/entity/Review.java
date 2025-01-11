package sesac_3rd.sesac_3rd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Review", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"place_id","user_id","review_content"})
})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId;  // 리뷰아이디 (PK)

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "place_id", nullable = false)  // 장소아이디 (외래 키)
    private Place place;  // Place 엔티티와 다대일 관계

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)  // 유저아이디 (외래 키)
    private User user;  // User 엔티티와 다대일 관계

    @Column(name = "star", nullable = false)
    private int star = 0;  // 별점 (기본값 0)

    @Column(name = "review_content", nullable = false, length = 500)
    private String reviewContent;  // 리뷰내용

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;  // 삭제여부 (기본값 FALSE)

    @Column(name = "createdAt", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;  // 생성일자

    @Column(name = "updatedAt", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;  // 수정일자

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", reviewContent='" + reviewContent + '\'' +
                ", star=" + star +
                '}';
    }
}
