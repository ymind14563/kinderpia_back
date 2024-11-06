package sesac_3rd.sesac_3rd.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyUserStatisticsDTO {
    private int month;              // 월
    private Long userCount;         // 해당 월의 가입자 수

    // 차트 출력을 위한 포맷팅 메서드
    public String getFormattedMonth() {
        return String.format("%02d", month);
    }
}
