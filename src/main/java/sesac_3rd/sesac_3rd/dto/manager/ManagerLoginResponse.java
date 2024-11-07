package sesac_3rd.sesac_3rd.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerLoginResponse {
    private boolean success;
    private String redirectUrl;
}
