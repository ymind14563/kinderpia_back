package sesac_3rd.sesac_3rd.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReportDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Report {
        private Long reportId;
        private Long chatMessageId;
        private Long reviewId;
        private Long meetingId;
        private Long reporterId;
        private Long reportedId;
        private Long reportReasonId;
        private String reportReasonName;
        private String reportMessageContent;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportList {
        private List<Report> reportList;
    }
}
