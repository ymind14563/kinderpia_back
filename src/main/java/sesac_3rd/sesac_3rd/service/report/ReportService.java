package sesac_3rd.sesac_3rd.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac_3rd.sesac_3rd.dto.report.ReportDTO;
import sesac_3rd.sesac_3rd.entity.*;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.mapper.report.ReportMapper;
import sesac_3rd.sesac_3rd.repository.*;
import sesac_3rd.sesac_3rd.repository.chat.ChatMessageRepository;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ReviewRepository reviewRepository;
    private final MeetingRepository meetingRepository;
    private final ReportReasonRepository reportReasonRepository;

    @Transactional
    public ReportDTO.Report createReportIfNotExist(ReportDTO.Report reportDto) {
        // 각 요소 존재하는지 확인 (신고자, 피신고자, 채팅메세지, 리뷰, 모임, 신고사유)
        User reporter = userRepository.findById(reportDto.getReporterId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

        User reported = userRepository.findById(reportDto.getReportedId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

        ChatMessage chatMessage = reportDto.getChatMessageId() != null
                ? chatMessageRepository.findById(reportDto.getChatMessageId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.CHATMSG_NOT_FOUND)) : null;

        Review review = reportDto.getReviewId() != null
                ? reviewRepository.findById(reportDto.getReviewId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.REVIEWID_NOT_FOUND)) : null;

        Meeting meeting = reportDto.getMeetingId() != null
                ? meetingRepository.findById(reportDto.getMeetingId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND)) : null;

        ReportReason reportReason = reportReasonRepository.findById(reportDto.getReportReasonId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.REPORTRS_NOT_FOUND));


        // 중복 신고 여부 확인
        boolean isExist = reportRepository.existsByReporterAndChatMessageOrReviewOrMeeting(reporter, chatMessage, review, meeting);
        if (isExist) {
            throw new CustomException(ExceptionStatus.DUPLICATE_REPORT);
        }


        // 신고 생성
        Report report = reportMapper.reportDTOtoEntity(reportDto, reporter, reported, chatMessage, review, meeting, reportReason);

        Report savedReport = reportRepository.save(report);

        return reportMapper.reportToReportResponseDto(savedReport);

    }
}