package sesac_3rd.sesac_3rd.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyUserStatisticsDTO {
    private int day;          // 날짜
    private Long userCount;          // 해당 일자의 가입자 수

    // 차트 출력을 위한 포맷팅 메서드
    public String getFormattedDay() {
        return String.format("%02d", day);
    }
}
