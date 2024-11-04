package sesac_3rd.sesac_3rd.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac_3rd.sesac_3rd.dto.report.ReportDTO;
import sesac_3rd.sesac_3rd.entity.*;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.mapper.report.ReportMapper;
import sesac_3rd.sesac_3rd.repository.*;
import sesac_3rd.sesac_3rd.repository.chat.ChatMessageRepository;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


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

    // 신고 생성
    @Transactional
    public ReportDTO.Report createReportIfNotExist(ReportDTO.Report reportDto) {
        // 각 요소 존재하는지 확인 (신고자, 피신고자, 채팅메세지, 리뷰, 모임, 신고사유)
        User reporter = userRepository.findById(reportDto.getReporterId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

        ChatMessage chatMessage = getChatMessageIfExists(reportDto.getChatMessageId());
        Review review = getReviewIfExists(reportDto.getReviewId());
        Meeting meeting = getMeetingIfExists(reportDto.getMeetingId());

        ReportReason reportReason = reportReasonRepository.findById(reportDto.getReportReasonId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.REPORT_RS_NOT_FOUND));

        // 피신고자 설정
        User reported = getReportedUser(chatMessage, review, meeting);

        // 중복 신고 여부 확인
        boolean isReportExist = isReportExist(reporter.getUserId(), reportDto.getChatMessageId(), reportDto.getReviewId(), reportDto.getMeetingId());

        if (isReportExist) {
            throw new CustomException(ExceptionStatus.DUPLICATE_REPORT);
        }

        // 신고 생성 및 저장
        Report report = reportMapper.reportDTOtoEntity(reportDto, reporter, reported, chatMessage, review, meeting, reportReason);
        Report savedReport = reportRepository.save(report);

        // 블랙리스트 확인 및 업데이트
        checkAndUpdateBlacklist(reported);

        return reportMapper.reportToReportResponseDto(savedReport);
    }


    // 신고된 채팅 메시지 목록 조회 (관리자 전용) - 페이지네이션 적용 (동적 설정)
    public PaginationResponseDTO<ReportDTO.ReportList> getChatMessageReports(int page, int size, String sortDirection, String sortProperty) {

        // 정렬 방향과 기준 필드를 동적으로 설정
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection).orElse(Sort.Direction.ASC);
        Sort sort = Sort.by(direction, sortProperty);

        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);

        // 신고된 채팅 메시지 목록 조회
        Page<Report> reportPage = reportRepository.findAllByChatMessageNotNull(pageRequest);
        ReportDTO.ReportList reportList = reportMapper.reportListToReportListResponseDto(reportPage.getContent());

        return new PaginationResponseDTO<>(reportList, reportPage);
    }


    // 신고된 리뷰 목록 조회 (관리자 전용) - 페이지네이션 적용 (동적 설정)
    public PaginationResponseDTO<ReportDTO.ReportList> getReviewReports(int page, int size, String sortDirection, String sortProperty) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection).orElse(Sort.Direction.ASC);
        Sort sort = Sort.by(direction, sortProperty);
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);

        Page<Report> reportPage = reportRepository.findAllByReviewNotNull(pageRequest);
        ReportDTO.ReportList reportList = reportMapper.reportListToReportListResponseDto(reportPage.getContent());

        return new PaginationResponseDTO<>(reportList, reportPage);
    }


    // 신고된 모임 목록 조회 (관리자 전용) - 페이지네이션 적용 (동적 설정)
    public PaginationResponseDTO<ReportDTO.ReportList> getMeetingReports(int page, int size, String sortDirection, String sortProperty) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection).orElse(Sort.Direction.ASC);
        Sort sort = Sort.by(direction, sortProperty);
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);

        Page<Report> reportPage = reportRepository.findAllByMeetingNotNull(pageRequest);
        ReportDTO.ReportList reportList = reportMapper.reportListToReportListResponseDto(reportPage.getContent());

        return new PaginationResponseDTO<>(reportList, reportPage);
    }






    // 블랙리스트 확인 및 업데이트
    private void checkAndUpdateBlacklist(User reported) {
        long reportCount = reportRepository.countByReported(reported);

        /*
         문제: `isBlacklist` 필드의 Getter/Setter 충돌 문제
         boolean 필드 `isBlacklist`에 대해 Lombok이 기본적으로 `isBlacklist()`와 `setBlacklist()` 메서드를 생성함.
         하지만 호출 시 직접 `getIsBlacklist()`를 사용할 경우 충돌 발생.

         해결방법: Lombok의 Getter/Setter 설정 방식 세 가지
         1. 체이닝 설정 추가
            - Lombok의 @Accessors(chain = true) 어노테이션을 사용해 `User` 엔티티에 체이닝 방식의 Getter/Setter 생성 가능.

         2. User Entity 에 직접 Getter/Setter 메서드 작성
            - `isBlacklist()`와 `setIsBlacklist()` 메서드를 수동으로 작성해 명확히 호출할 수 있도록 설정 가능.

         3. Lombok 자동 생성 메서드 사용 (현재 방식)
            - `User` 엔티티 수정 없이 Lombok이 자동 생성한 `isBlacklist()`와 `setBlacklist()` 메서드를 그대로 활용함.
        */

        if (reportCount >= 5 && !reported.isBlacklist()) {
            reported.setBlacklist(true);
            userRepository.save(reported);
        }
    }

    // 채팅 메세지 존재 확인
    private ChatMessage getChatMessageIfExists(Long chatMessageId) {
        return chatMessageId != null ? chatMessageRepository.findById(chatMessageId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.CHATMSG_NOT_FOUND)) : null;
    }

    // 리뷰 존재 확인
    private Review getReviewIfExists(Long reviewId) {
        return reviewId != null ? reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.REVIEWID_NOT_FOUND)) : null;
    }

    // 미팅 존재 확인
    private Meeting getMeetingIfExists(Long meetingId) {
        return meetingId != null ? meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND)) : null;
    }

    // 피신고자 지정
    private User getReportedUser(ChatMessage chatMessage, Review review, Meeting meeting) {
        // 신고 대상이 없는 경우
        if (chatMessage == null && review == null && meeting == null) {
            throw new CustomException(ExceptionStatus.INVALID_REPORT_TARGET);
        }

        // 지정
        User reported = (chatMessage != null) ? chatMessage.getSender() :
                (review != null) ? review.getUser() :
                        (meeting != null) ? meeting.getUser() : null;

        // 피신고자가 존재하지 않는 경우
        if (reported == null) {
            throw new CustomException(ExceptionStatus.USER_NOT_FOUND);
        }

        return reported;
    }

    // 중복 신고 확인
    public boolean isReportExist(Long reporterId, Long chatMessageId, Long reviewId, Long meetingId) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));


        ChatMessage chatMessage = chatMessageId != null ? getChatMessageIfExists(chatMessageId) : null;
        Review review = reviewId != null ? getReviewIfExists(reviewId) : null;
        Meeting meeting = meetingId != null ? getMeetingIfExists(meetingId) : null;

        // 아무런 신고가 없는 경우
        if (chatMessage == null && review == null && meeting == null) {
            throw new CustomException(ExceptionStatus.NO_REPORT_FOUND);
        }

        if (chatMessage != null) {
            return reportRepository.existsByReporterAndChatMessage(reporter, chatMessage);
        } else if (review != null) {
            return reportRepository.existsByReporterAndReview(reporter, review);
        } else if (meeting != null) {
            return reportRepository.existsByReporterAndMeeting(reporter, meeting);
        }

        return false;
    }




}