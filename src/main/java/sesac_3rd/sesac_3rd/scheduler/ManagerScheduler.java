package sesac_3rd.sesac_3rd.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sesac_3rd.sesac_3rd.service.user.ManagerService;

import java.time.LocalDate;

@Component
public class ManagerScheduler {

    @Autowired
    private ManagerService managerService;

    // 매일 새벽 1시 30분에 Redis 캐싱 데이터 갱신
    @Scheduled(cron = "0 30 1 * * ?")
    public void updateManagerData() {
        try {
            System.out.println("Manager 관련 Redis 데이터 갱신 시작");

            // 전체 사용자 수 갱신
            managerService.totalUserCount();
            System.out.println("전체 사용자 수 갱신 완료");

            // 월별 가입자 수 갱신
            managerService.getMonthlyStat(LocalDate.now().getYear());
            System.out.println("월별 가입자 수 갱신 완료");

            // 일별 가입자 수 갱신
            managerService.getDailyStat(LocalDate.now().toString().substring(0, 7));
            System.out.println("일별 가입자 수 갱신 완료");

            // 총 모임 수 갱신
            managerService.ongoingMeetingCount();
            System.out.println("총 모임 수 갱신 완료");

            // 총 신고 수 갱신
            managerService.getTotalReportCnt();
            System.out.println("총 신고 수 갱신 완료");

            System.out.println("Manager 관련 Redis 데이터 갱신 완료");
        } catch (Exception e) {
            System.err.println("Manager 데이터 갱신 중 오류 발생: " + e.getMessage());
        }
    }
}
