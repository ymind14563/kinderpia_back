package sesac_3rd.sesac_3rd.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingCategoryCountDTO {
    private Long categoryId;
    private String categoryName;
    private Long meetingCount;
}
