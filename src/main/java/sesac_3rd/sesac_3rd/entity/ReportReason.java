package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ReportReason")
@Entity
public class ReportReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_rs_id", nullable = false)
    private Long reportRsId;  // 신고사유아이디 (PK)

    @Column(name = "report_rs_name", nullable = false, length = 20, unique = true)
    private String reportRsName;  // 신고사유명
}
