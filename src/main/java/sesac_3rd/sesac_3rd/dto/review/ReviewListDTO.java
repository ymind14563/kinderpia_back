package sesac_3rd.sesac_3rd.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.entity.Review;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewListDTO {
    private List<Review> reviews;
    private Integer averageStar;
}
