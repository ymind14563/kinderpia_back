package sesac_3rd.sesac_3rd.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import sesac_3rd.sesac_3rd.entity.Meeting;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MeetingRepositoryTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Test
    public void testSearchPerformance() {
        String keyword = "시";
        Pageable pageable = PageRequest.of(0, 10);

        long startTime = System.currentTimeMillis();

        // LIKE 방식 테스트
        meetingRepository.findByMeetingTitleOrDistrict(keyword, pageable);
        long likeDuration = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();

        // Full Text Index 방식 테스트
        meetingRepository.searchMeetingsByKeywordUsingFullTextIndex(keyword, pageable);
        long fullTextDuration = System.currentTimeMillis() - startTime;

        System.out.println("LIKE 검색 소요 시간(ms): " + likeDuration);
        System.out.println("Full Text Index 검색 소요 시간(ms): " + fullTextDuration);

    }
}
