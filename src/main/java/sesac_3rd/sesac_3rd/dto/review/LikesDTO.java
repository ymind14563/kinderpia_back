package sesac_3rd.sesac_3rd.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikesDTO {
    private Long likesId;
    private Long reviewId;
    private Long userId;
}
