package sesac_3rd.sesac_3rd.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.dto.manager.DailyUserStatisticsDTO;
import sesac_3rd.sesac_3rd.dto.manager.MeetingCategoryCountDTO;
import sesac_3rd.sesac_3rd.dto.manager.MonthlyUserStatisticsDTO;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.entity.Manager;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.repository.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    UserMeetingRepository userMeetingRepository;

    @Autowired
    ReportRepository reportRepository;

    // 관리자 로그인
    @Override
    public void managerLogin(String loginId, String password) {
        final Manager getManager = managerRepository.findByLoginIdAndPw(loginId, password);
        if (getManager == null) {
            throw new CustomException(ExceptionStatus.MANAGER_NOT_FOUND);  // 404
        }
    }

    // 전체 가입자 수(탈퇴한 사용자 제외)
    @Override
    public long totalUserCount() {
        return userRepository.countByIsDeletedFalse();
    }

    // 일별 가입자 수
    @Override
    public Map<String, Object> getDailyStat(String yearMonth) {
        // yearMonth : yyyy-mm
        String[] parts = yearMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        List<DailyUserStatisticsDTO> stats = userRepository.findDailyStatByYearAndMonth(year, month);

        Map<String, Object> response = new HashMap<>();
        response.put("labels", stats.stream()
                .map(stat -> yearMonth + "-" + stat.getFormattedDay())
                .collect(Collectors.toList()));
        response.put("data", stats.stream()
                .map(DailyUserStatisticsDTO::getUserCount)
                .collect(Collectors.toList()));

        return response;
    }

    // 월별 가입자 수
    @Override
    public Map<String, Object> getMonthlyStat(int year) {
        List<MonthlyUserStatisticsDTO> stats = userRepository.findMonthlyStatByYear(year);

        Map<String, Object> response = new HashMap<>();
        response.put("labels", stats.stream()
                .map(stat -> year + "-" + stat.getFormattedMonth())
                .collect(Collectors.toList()));
        response.put("data", stats.stream()
                .map(MonthlyUserStatisticsDTO::getUserCount)
                .collect(Collectors.toList()));

        return response;
    }

    // 총 모임 수('모집중' 수)
    @Override
    public long ongoingMeetingCount() {
        long ongoingMeetingCnt = meetingRepository.countByMeetingStatus(MeetingStatus.ONGOING);
        return ongoingMeetingCnt;
    }

    // 일별 모임 참여자 수(수락된 사람들)
    @Override
    public Map<LocalDate, Long> getDailyAcceptedUsers() {
        List<Object[]> results = userMeetingRepository.countDailyAcceptedUsers();
        Map<LocalDate, Long> dailyCnt = new HashMap<>();

        for (Object[] result : results) {
            Date sqlDate = (Date) result[0];
            LocalDate date = sqlDate.toLocalDate();
            Long count = (Long) result[1];
            dailyCnt.put(date, count);
        }
        return dailyCnt;
    }

    // 카테고리별 모임 수('삭제'된 모임 제외)
    @Override
    public List<MeetingCategoryCountDTO> getMeetingCntByCategory() {
        List<Object[]> results = meetingRepository.countMeetingsByCategory();
        List<MeetingCategoryCountDTO> categoryCountDTOS = new ArrayList<>();

        for (Object[] result : results) {
            MeetingCategoryCountDTO dto = new MeetingCategoryCountDTO(
                    (Long) result[0],
                    (String) result[1],
                    (Long) result[2]
            );
            categoryCountDTOS.add(dto);
        }
        return categoryCountDTOS;
    }

    // 총 신고수
    @Override
    public long getTotalReportCnt() {
        long reportCnt = reportRepository.countById();
        return reportCnt;
    }

    // 블랙리스트 추가
    @Override
    public void insertBlacklist(String userId) {

    }

    // 블랙리스트 목록 조회
    @Override
    public List<UserDTO> getBlacklistList() {
        return List.of();
    }
}
