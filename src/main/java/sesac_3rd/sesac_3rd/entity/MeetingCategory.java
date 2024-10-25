package sesac_3rd.sesac_3rd.entity;
import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MeetingCategory")
@Entity
public class MeetingCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_ctg_id", nullable = false)
    private Long meetingCtgId;  // 모임카테고리아이디 (PK)

    @Column(name = "meeting_ctg_name", nullable = false, length = 20, unique = true)
    private String meetingCtgName;  // 모임카테고리명
}
