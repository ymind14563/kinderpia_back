package sesac_3rd.sesac_3rd.dto.place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.entity.PlaceCategory;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDTO {
    private Long placeId;
    private String placeCategoryName;
    private String placeName;
    private String location;
    private String detailAddress;
    private String operatingDate;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String placeImg;
    private boolean isPaid;
    private String homepageUrl;
    private String placeNum;
}
