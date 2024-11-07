package sesac_3rd.sesac_3rd.service.user;

import sesac_3rd.sesac_3rd.dto.manager.MeetingCategoryCountDTO;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ManagerService {
    // 관리자 로그인
    void managerLogin(String loginId, String password);

    // 전체 가입자 수(탈퇴한 사용자 제외)
    long totalUserCount();

    // 월별 가입자 수
    Map<String, Object> getMonthlyStat(int year);

    // 일별 가입자 수
    Map<String, Object> getDailyStat(String yearMonth);

    // 총 모임 수('모집중' 수)
    long ongoingMeetingCount();

    // 일별 모임 참여자 수(수락된 사람들)
    Map<LocalDate, Long> getDailyAcceptedUsers();

    // 카테고리별 모임 수('삭제'된 모임 제외)
    List<MeetingCategoryCountDTO> getMeetingCntByCategory();

    // 전체 신고 수
    long getTotalReportCnt();

    // 블랙리스트 추가
    void insertBlacklist(String userId);

    // 블랙리스트 목록 조회
    List<UserDTO> getBlacklistList();


}
