package sesac_3rd.sesac_3rd.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.entity.Review;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUserDTO {
    private Review review;
    private long userId;
    private String nickname;
    private String profileImg;
    private boolean isBlacklist;
    private long likeCount;
}