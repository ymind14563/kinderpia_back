package sesac_3rd.sesac_3rd.service.user;

import sesac_3rd.sesac_3rd.dto.user.UserDTO;

import java.util.List;

public interface ManagerService {
    // 블랙리스트 추가
    void insertBlacklist(String userId);

    // 블랙리스트 목록 조회
    List<UserDTO> getBlacklistList();

}
