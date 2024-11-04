package sesac_3rd.sesac_3rd.dto.place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceWithCategoryDTO {
    private Long placeId;
    private String placeCtgName; // 카테고리 이름
    private String placeName;
    private String location;
    private String detailAddress;
    private String operatingDate;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String placeImg;
    private String homepageUrl;
    private String placeNum;
    private boolean paid;
}
