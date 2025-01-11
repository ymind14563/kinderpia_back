package sesac_3rd.sesac_3rd.dto.place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopularPlaceDTO {
    private Long placeId;
    private String placeName;
    private String placeImg;
    private boolean isPaid;
    private String placeCtgName;
    private Integer averageStar;
    private Integer totalReviewCount;
}


// 인기장소는 기존 PlaceReviewDTO의 변수를 활용하는데
// 인기장소는 변수 전부를 사용하는 것이 아닌 일부만 사용하므로
// 명시적으로 생성자를 선언하지 않으면 오류 발생함.
// 그래서 PopularPlaceDTO 처럼 새로이 생성하거나, (선택방법)
// PlaceReviewDTO 내부에 this. 를 활용하여 만들어 줘야 정상 작동함.