package sesac_3rd.sesac_3rd.dto.place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDTO {
    private Long placeId;
    private Long placeCtgId;
    private String placeName;
    private String location;
    private String detailAddress;
    private String operatingDate;
    private BigDecimal latitude;
    private BigDecimal longtitude;
    private String placeImg;
    private boolean isPaid;
    private String homepageUrl;
    private String placeNum;
}
