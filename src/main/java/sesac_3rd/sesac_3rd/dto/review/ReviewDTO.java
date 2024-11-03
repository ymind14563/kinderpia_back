package sesac_3rd.sesac_3rd.dto.review;


import lombok.*;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.entity.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long reviewId;
    private Place place;
    private User user;
    private int star;
    private String reviewContent;
    private boolean isDeleted;

}
