package sesac_3rd.sesac_3rd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.dto.report.ReportDTO;
import sesac_3rd.sesac_3rd.dto.report.ReportRequestDTO;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.service.report.ReportService;


@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    // 신고 생성
    @PostMapping
    public ResponseEntity<ResponseHandler<ReportDTO.Report>> createReport(@AuthenticationPrincipal Long userId,
                                                                          @RequestBody ReportDTO.Report reportDto) {

        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        // token 의 userId를 DTO의 reporterId 직접 설정 - 값이 다를 가능성 애초에 차단
        reportDto.setReporterId(userId);

        ReportDTO.Report createdReport = reportService.createReportIfNotExist(reportDto);
        return ResponseHandler.response(createdReport, HttpStatus.CREATED, "신고 성공");
    }



    // 중복 신고 확인
    @PostMapping("/check")
    public ResponseEntity<ResponseHandler<Boolean>> checkReport(@AuthenticationPrincipal Long userId,
                                                                @RequestBody ReportRequestDTO reportRequestDto) {

        // 로그인하지 않은 사용자일 경우 기본값 false 로 지정
        if (userId == null) {
            return ResponseHandler.response(false, HttpStatus.OK, "로그인 하지 않음");
        }

        reportRequestDto.setReporterId(userId);

        // reportRequestDto.getReporterId() 대신 바로 userId 넣어도 되지만, 가독성상 유지
        boolean isReportCheck = reportService.isReportExist(reportRequestDto.getReporterId(),
                reportRequestDto.getChatMessageId(),
                reportRequestDto.getReviewId(),
                reportRequestDto.getMeetingId());

        return ResponseHandler.response(isReportCheck, HttpStatus.OK, "중복 신고 여부 확인 완료");
    }


    // 신고된 채팅 메시지 목록 조회 (관리자 전용) - 페이지네이션 적용 (동적 설정)
    // /api/report/chatmsg?page=1&size=10&direction=ASC&property=createdAt
    @GetMapping("/chatmsg")
    public PaginationResponseDTO<ReportDTO.ReportList> getChatMessageReports(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "direction",defaultValue = "ASC") String direction,
            @RequestParam(value = "property", defaultValue = "createdAt") String property) {

        return reportService.getChatMessageReports(page, size, direction, property);
    }

    // 신고된 리뷰 목록 조회 (관리자 전용) - 페이지네이션 적용 (동적 설정)
    @GetMapping("/review")
    public PaginationResponseDTO<ReportDTO.ReportList> getReviewReports(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "direction",defaultValue = "ASC") String direction,
            @RequestParam(value = "property", defaultValue = "createdAt") String property) {

        return reportService.getReviewReports(page, size, direction, property);
    }

    // 신고된 모임 목록 조회 (관리자 전용) - 페이지네이션 적용 (동적 설정)
    @GetMapping("/meeting")
    public PaginationResponseDTO<ReportDTO.ReportList> getMeetingReports(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "direction",defaultValue = "ASC") String direction,
            @RequestParam(value = "property", defaultValue = "createdAt") String property) {

        return reportService.getMeetingReports(page, size, direction, property);
    }



}
