package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Likes")
@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id", nullable = false) // Primary Key
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false) // Foreign Key
    private Review review;   // 리뷰와의 관계 설정

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign Key
    private User user;    // 사용자와의 관계 설정
}
