package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.dto.manager.ManagerDTO;
import sesac_3rd.sesac_3rd.dto.manager.ManagerLoginResponse;
import sesac_3rd.sesac_3rd.dto.manager.MeetingCategoryCountDTO;
import sesac_3rd.sesac_3rd.dto.user.LoginResponse;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.service.user.ManagerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    @Autowired
    ManagerService managerService;

    // 관리자 로그인
    @PostMapping
    public ResponseEntity<ManagerLoginResponse> managerLogin(@RequestBody ManagerDTO dto) {
        managerService.managerLogin(dto.getManagerLoginId(), dto.getManagerPw());

        ManagerLoginResponse response = new ManagerLoginResponse();
        response.setRedirectUrl("/admin");
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }

    // 전체 가입자 수(탈퇴한 사용자 제외)
    @GetMapping("/total/userCnt")
    public ResponseEntity<ResponseHandler<Long>> totalUserCount() {
        Long userCnt = managerService.totalUserCount();

        return ResponseHandler.response(userCnt, HttpStatus.OK, "전체 사용자 수");
    }

    // 월별 가입자 수
    // 월별은 년도 받고, 일별은 년월 받고
    @GetMapping("/total/userCnt/monthly")
    public ResponseEntity<ResponseHandler<Map<String, Object>>> totalUserCountGroupByMonthly(@RequestParam(value = "year", required = true) int year) {
        Map<String, Object> stats = managerService.getMonthlyStat(year);

        return ResponseHandler.response(stats, HttpStatus.OK, "월별 가입자 수");
    }

    // 일별 가입자 수
    // yearMonth : yyyy-mm
    @GetMapping("/total/userCnt/daily")
    public ResponseEntity<ResponseHandler<Map<String, Object>>> totalUserCountGroupByDaily(@RequestParam(value = "month", required = true) String yearMonth) {
        Map<String, Object> stats = managerService.getDailyStat(yearMonth);

        return ResponseHandler.response(stats, HttpStatus.OK, "일별 가입자 수");
    }

    // 총 모임 수('모집중' 수)
    @GetMapping("/total/meetingCnt")
    public ResponseEntity<ResponseHandler<Long>> totalMeetingCount() {
        Long meetingCnt = managerService.ongoingMeetingCount();

        return ResponseHandler.response(meetingCnt, HttpStatus.OK, "모집중 모임 수");
    }

    // 일별 모임 참여자 수(수락된 사람들)
    @GetMapping("/acceptedUserCnt/daily")
    public ResponseEntity<ResponseHandler<Map<LocalDate, Long>>> getDailyAcceptedUsers() {
        Map<LocalDate, Long> userCnt = managerService.getDailyAcceptedUsers();

        return ResponseHandler.response(userCnt, HttpStatus.OK, "일별 모임 참여쟈 수");
    }

    // 카테고리별 모임 수('삭제'된 모임 제외)
    @GetMapping("/meetingCnt/category")
    public ResponseEntity<ResponseHandler<List<MeetingCategoryCountDTO>>> getMeetingCntByCategory() {
        List<MeetingCategoryCountDTO> meetingCnt = managerService.getMeetingCntByCategory();

        return ResponseHandler.response(meetingCnt, HttpStatus.OK, "카테고리별 모임 수");
    }

    // 총 신고 수
    @GetMapping("/total/reportCnt")
    public ResponseEntity<ResponseHandler<Long>> getReportCnt() {
        Long reportCnt = managerService.getTotalReportCnt();

        return ResponseHandler.response(reportCnt, HttpStatus.OK, "총 신고 수");
    }
}
