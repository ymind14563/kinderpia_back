package sesac_3rd.sesac_3rd.scheduler;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.service.place.PlaceService;

@Service
public class PopularPlaceScheduler {

    @Autowired
    private PlaceService placeService;

    // 매일 새벽 1시에 실행 (cron: 초, 분, 시, 일, 월, 요일)
    @Scheduled(cron = "0 0 1 * * ?")
    @SchedulerLock(name = "updatePopularPlacesData", lockAtLeastFor = "PT1M", lockAtMostFor = "PT5M")
    public void updatePopularPlaces() {
        try {
            System.out.println("인기 장소 갱신 시작");

            // DB에서 인기 장소를 가져와 Redis에 저장
            placeService.savePopularPlacesToRedis(placeService.getPopularPlaces());
            System.out.println("인기 장소 갱신 완료");
        } catch (Exception e) {
            System.err.println("인기 장소 갱신 중 오류 발생: " + e.getMessage());
        }
    }
}
