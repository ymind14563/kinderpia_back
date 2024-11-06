package sesac_3rd.sesac_3rd.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerDTO {
    private String managerLoginId;
    private String managerPw;
}
