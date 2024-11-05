package sesac_3rd.sesac_3rd.dto.place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.entity.Place;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceReviewDTO {
    private Place place;
    private String placeCtgName;
    private Integer averageStar;
}
