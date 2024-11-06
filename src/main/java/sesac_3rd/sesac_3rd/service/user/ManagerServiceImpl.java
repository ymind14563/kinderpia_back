package sesac_3rd.sesac_3rd.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.manager.DailyUserStatisticsDTO;
import sesac_3rd.sesac_3rd.dto.manager.MonthlyUserStatisticsDTO;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.entity.Manager;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.repository.ManagerRepository;
import sesac_3rd.sesac_3rd.repository.UserRepository;

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
