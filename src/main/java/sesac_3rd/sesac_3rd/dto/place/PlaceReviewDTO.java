package sesac_3rd.sesac_3rd.dto.place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.entity.Place;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceReviewDTO {
    private Long placeId;
    private String placeName;
    private String location;
    private String detailAddress;
    private String operatingDate;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String placeImg;
    private String homepageUrl;
    private String placeNum;
    private boolean isPaid;
    private String placeCtgName;
    private Integer averageStar;
    private Integer totalReviewCount;
}