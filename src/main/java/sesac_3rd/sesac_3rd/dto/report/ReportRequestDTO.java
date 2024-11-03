package sesac_3rd.sesac_3rd.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDTO {
    private Long reportId;
    private Long chatMessageId;
    private Long reviewId;
    private Long meetingId;
    private Long reporterId;
}
