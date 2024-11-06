package sesac_3rd.sesac_3rd.service.user;

import org.springframework.web.multipart.MultipartFile;
import sesac_3rd.sesac_3rd.dto.user.*;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;

import java.util.List;

public interface UserService {
    // 로그인
    LoginResponse userLogin(String loginId, String userPw);

    // 회원가입
    UserResponseDTO register(UserFormDTO dto);

    // 회원가입 - 닉네임 중복 검사
    void isNicknameDuplicate(String nickname);

    // 회원가입 - 아이디 중복 검사
    void isLoginIdDuplicate(String loginId);

    // 회원가입 - 이메일 중복 검사
    void isEmailDuplicate(String email);

    // 회원가입 - 전화번호 중복 검사
    void isPhonenumDuplicate(String phoneNum);

    // 회원 정보 단건 조회
    UserDTO getUser(Long userId);

    // 회원 정보 수정
    UserDTO updateUser(Long userId, UserFormDTO dto);

    // 회원 정보 수정(프로필 이미지)
    UserDTO updateUserProfileImg(Long userId, MultipartFile image);

    // 회원 탈퇴
    void deleteUser(Long userId);

    // 비밀번호 일치 확인(회원 수정, 탈퇴시)
    void checkUserPw(Long userId, String userPw);

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장인 모임) - 페이지네이션
    PaginationResponseDTO<UserMeetingListDTO> getUserLeaderMeetingList(Long userId, int size, int page);

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장이거나 모임에 속해 있는 경우) - 페이지네이션
    PaginationResponseDTO<UserMeetingListDTO> getUserMeetingList(Long userId, int size, int page);

    // 사용자 모임 일정 목록 조회(사용자가 모임장이거나 속해있는 모임, 삭제된 모임 제외)
    List<UserMeetingTimeListDTO> getUserMeetingScheduleList(Long userId);

    // 사용자 리뷰 목록 조회(장소 정보까지 같이) - 페이지네이션 할건지?
    PaginationResponseDTO<UserReviewDTO> getUserReviewList(Long userId, int size, int page);

    // 모임상세 접근시 사용자 상태 조회(신고여부, 신청여부, 수락여부)
    UserMeetingStatusDTO getUserMeetingStatus(Long userId, Long meetingId);

    // 승인 대기자 목록(각각 모임에 대한)
    List<UserResponseDTO> getUserMeetingApprovalList(Long meetingId);
}
