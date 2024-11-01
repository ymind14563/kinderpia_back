package sesac_3rd.sesac_3rd.mapper.report;

import org.springframework.stereotype.Component;
import sesac_3rd.sesac_3rd.dto.report.ReportDTO;
import sesac_3rd.sesac_3rd.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReportMapper {

    public ReportDTO.Report reportToReportResponseDto(Report report) {
        ReportDTO.Report.ReportBuilder reportBuilder = ReportDTO.Report.builder()
                .reportId(report.getReportId())
                .reporterId(report.getReporter().getUserId())
                .reportedId(report.getReported().getUserId())
                .reportReasonId(report.getReportReason().getReportRsId())
                .reportReasonName(report.getReportReason().getReportRsName())
                .reportMessageContent(report.getReportMessageContent())
                .createdAt(report.getCreatedAt());

        // 조건부로 추가 (값이 있는 거만)
        if (report.getChatMessage() != null) {
            reportBuilder.chatMessageId(report.getChatMessage().getChatmsgId());
        }
        if (report.getReview() != null) {
            reportBuilder.reviewId(report.getReview().getReviewId());
        }
        if (report.getMeeting() != null) {
            reportBuilder.meetingId(report.getMeeting().getMeetingId());
        }

        return reportBuilder.build();
    }

    public ReportDTO.ReportList reportListToReportListResponseDto(List<Report> reports) {
        List<ReportDTO.Report> reportList = reports.stream()
                .map(this::reportToReportResponseDto)
                .collect(Collectors.toList());
        return ReportDTO.ReportList.builder()
                .reportList(reportList)
                .build();
    }

    public Report reportDTOtoEntity(ReportDTO.Report reportDto, User reporter, User reported,
                                 ChatMessage chatMessage, Review review, Meeting meeting, ReportReason reportReason) {
        Report report = new Report();
        report.setReporter(reporter);
        report.setReported(reported);
        report.setChatMessage(chatMessage);
        report.setReview(review);
        report.setMeeting(meeting);
        report.setReportReason(reportReason);
        report.setReportMessageContent(reportDto.getReportMessageContent());
        report.setCreatedAt(LocalDateTime.now());
        return report;
    }

}
