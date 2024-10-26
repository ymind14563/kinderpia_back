package sesac_3rd.sesac_3rd.service.user;

import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService{
    // 블랙리스트 추가
    @Override
    public void insertBlacklist(String userId) {

    }

    // 블랙리스트 목록 조회
    @Override
    public List<UserDTO> getBlacklistList() {
        return List.of();
    }
}
