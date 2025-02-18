package sesac_3rd.sesac_3rd.service.user;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sesac_3rd.sesac_3rd.config.security.TokenProvider;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.dto.user.*;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.entity.Review;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.entity.UserMeeting;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.mapper.user.UserMapper;
import sesac_3rd.sesac_3rd.repository.MeetingRepository;
import sesac_3rd.sesac_3rd.repository.ReportRepository;
import sesac_3rd.sesac_3rd.repository.UserMeetingRepository;
import sesac_3rd.sesac_3rd.repository.UserRepository;
import sesac_3rd.sesac_3rd.service.s3.S3Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMeetingRepository userMeetingRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    S3Service s3Service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    // 로그인
    @Override
    public LoginResponse userLogin(final String loginId, final String userPw) {
        final User originUser = userRepository.findByLoginId(loginId);
        // 로그인 아이디가 없을때
        // 커스텀에러는 '아이디 또는 비밀번호를 찾을 수 없습니다' 하나로 통일 시켜도 될듯
        // 로직 다시 확인
        if (originUser == null) {
            throw new CustomException(ExceptionStatus.USER_NOT_FOUND);   // 404
        }
        // 비번 불일치
        if (!passwordEncoder.matches(userPw, originUser.getUserPw())) {
            throw new CustomException(ExceptionStatus.INVALID_PASSWORD);  // 409
        }
        // 로그인 하려는 사용자가 탈퇴 여부가 true일때
        if (originUser.isDeleted()) {
            throw new CustomException(ExceptionStatus.WITHDRAWN_USER); // 403
        }
        // 비번 일치
        // 무슨 데이터 리턴할지는 좀 더 고민
        // 리턴에 헤더에 토큰값 넣어서 리턴하고 로그인 여부 true/false만 body에 넣어서
        final String token = tokenProvider.create(originUser);
//        LoginFormDTO formDTO = UserMapper.toLoginFormDTO(originUser);
        return new LoginResponse(token, true);

    }

    // 회원가입
    @Override
    public UserResponseDTO register(UserFormDTO dto) {
        log.info("register user");
        validatePassword(dto.getUserPw());   // 비번만 유효성 검사
        // 중복검사는 따로 안함
        // 비번 인코딩 후 insert
        String encodedPw = passwordEncoder.encode(dto.getUserPw());
        User newUser = UserMapper.toUserEntityForSignup(dto, encodedPw);
        User savedUser = userRepository.save(newUser);

        return UserMapper.toResponseDTO(savedUser);
    }

    // 회원가입 - 닉네임 중복 검사
    @Override
    public void isNicknameDuplicate(String nickname) {
        log.info("check nickname duplicated");
        validateNickname(nickname);
        boolean isDuplicated = userRepository.existsByNickname(nickname);
        if (isDuplicated) {
            throw new CustomException(ExceptionStatus.DUPLICATE_NICKNAME);
        }
    }

    // 회원가입 - 아이디 중복 검사
    @Override
    public void isLoginIdDuplicate(String loginId) {
        log.info("check loginid duplicated");
        validateLoginId(loginId);
        boolean isDuplicated = userRepository.existsByLoginId(loginId);
        if (isDuplicated) {
            throw new CustomException(ExceptionStatus.DUPLICATE_LOGIN_ID);
        }
    }

    // 회원가입 - 이메일 중복 검사
    @Override
    public void isEmailDuplicate(String email) {
        log.info("check email duplicated");
        validateEmail(email);
        boolean isDuplicated = userRepository.existsByEmail(email);
        if (isDuplicated) {
            throw new CustomException(ExceptionStatus.DUPLICATE_EMAIL);
        }
    }

    // 회원가입 - 전화번호 중복 검사
    @Override
    public void isPhonenumDuplicate(String phoneNum) {
        log.info("check phonenum duplicated");
        validatePhoneNumber(phoneNum);
        boolean isDuplicated = userRepository.existsByPhoneNum(phoneNum);
        if (isDuplicated) {
            throw new CustomException(ExceptionStatus.DUPLICATE_PHONE);
        }
    }


    // 회원 탈퇴
    // 모임 업데이트는 아직 안함
    @Override
    public void deleteUser(final Long userId) {
        // 사용자가 입력한 비번이 해당 사용자의 비번이어야 함
        User originUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));  // 404


        // 탈퇴한 사람이 작성한 리뷰나 모임, 채팅은 삭제하지 않음
        // 모임글은 '닫힘 모임' 처리 -> 탈퇴한 사람이 작성한 모임 중 '모집중' 모임이 모두 '모임종료'(END)로 업데이트
        List<Meeting> userMeetings = meetingRepository.findOngoingMeetingsByUserId(userId);
        if (!userMeetings.isEmpty()) {
            for (Meeting meeting : userMeetings) {
                meeting.setMeetingStatus(MeetingStatus.END);
                meetingRepository.save(meeting);
            }
        }

        // 해당 사용자의 탈퇴 여부를 true로 변경
        originUser.setDeleted(true);
        userRepository.save(originUser);
    }

    // 회원 정보 단건 조회
    @Override
    @Transactional(readOnly = true)
    public UserDTO getUser(Long userId) {
        log.info("get user : {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));  //new CustomException(ErrorCode.USER_NOT_FOUND)
        UserDTO userDTO = UserMapper.toUserDTO(user);
        return userDTO;
    }

    // 회원 정보 수정
    // 닉네임, 비번, 전번 수정 가능
    // 바꿀 수 있는 4개 컬럼 중 일부만 바뀌어도 수정 되도록(해당 컬럼에 값 있는지 없는지 확인 필요)
    @Override
    public UserDTO updateUser(Long userId, UserFormDTO dto) {
        log.info("update user : {}", userId);
        // 1. 사용자 조회
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

        // 2. 각 필드 유효성 검사
        // 닉네임 수정
        if (StringUtils.hasText(dto.getNickname())) {
            validateNickname(dto.getNickname());
            log.info("nickname update...");
            existingUser.setNickname(dto.getNickname());
        }

        // 비번 수정
        if (StringUtils.hasText(dto.getUserPw())) {
            validatePassword(dto.getUserPw());
            String encodedPw = passwordEncoder.encode(dto.getUserPw());
            log.info("password update...");
            existingUser.setUserPw(encodedPw);
        }

        // 전화번호 수정
        if (StringUtils.hasText(dto.getPhoneNum())) {
            validatePhoneNumber(dto.getPhoneNum());
            log.info("phonenum update...");
            existingUser.setPhoneNum(dto.getPhoneNum());
        }

        // 수정 날짜 업데이트
        existingUser.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toUserDTO(updatedUser);
    }

    // 회원 정보 수정(프로필 이미지)
    @Override
    public UserDTO updateUserProfileImg(Long userId, MultipartFile image) {
        log.info("update profileImg ././././././././  " + userId);

        // 1. 사용자 조회
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

        // 프로필 이미지 수정
        log.info("profileimg update...");

        // 기존 이미지가 있으면 S3에서 삭제
        if (StringUtils.hasText(existingUser.getProfileImg())) {
            log.info("delete image .///././././.");
            s3Service.deleteImgFromS3(existingUser.getProfileImg());
        }
        // 새 이미지 업로드 후 url 저장
        String imgUrl = s3Service.upload(image);
        log.info("image upload ()(*)(*)(*)(*)(*)(*" + imgUrl);
        existingUser.setProfileImg(imgUrl);
        existingUser.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(existingUser);

        return UserMapper.toUserDTO(updatedUser);
    }

    // 비밀번호 일치 확인(회원 수정, 탈퇴시)
    @Override
    public void checkUserPw(Long userId, String userPw) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));  // 404

        // 비번 유효성 검사
        validatePassword(userPw);

        // 비번 불일치
        if (!passwordEncoder.matches(userPw, findUser.getUserPw())) {
            throw new CustomException(ExceptionStatus.INVALID_PASSWORD);  // 409
        }
    }

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장이거나 모임에 속해 있는 경우) - 페이지네이션
    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDTO<UserMeetingListDTO> getUserMeetingList(Long userId, int size, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<UserMeetingListDTO> meetingPage = userRepository.meetingFindByUserId(userId, validMeetingStatus(), pageRequest);

        return new PaginationResponseDTO<>(meetingPage.getContent(), meetingPage);
    }

    // 사용자 모임 목록 조회(모임 삭제 상태 제외하고, 사용자가 모임장인 모임) - 페이지네이션
    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDTO<UserMeetingListDTO> getUserLeaderMeetingList(Long userId, int size, int page) {

        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Meeting> meetingPage = userRepository.meetingFindByLeaderUserId(userId, validMeetingStatus(), pageRequest);

        // meeting entity를 UserMeetingListDTO로 변환
        List<UserMeetingListDTO> userMeetingList = meetingPage.getContent().stream()
                .map(UserMapper::toUserMeetingListDTO)
                .collect(Collectors.toList());

        return new PaginationResponseDTO<>(userMeetingList, meetingPage);
    }

    // 사용자 모임 일정 목록 조회(사용자가 모임장이거나 속해있는 모임, 삭제된 모임 제외)
    @Override
    @Transactional(readOnly = true)
    public List<UserMeetingTimeListDTO> getUserMeetingScheduleList(Long userId) {

        List<UserMeetingTimeListDTO> getScheduleList = userRepository.meetingTimeFindByUserId(userId, validMeetingStatus());

        return getScheduleList;
    }

    // 사용자 리뷰 목록 조회(장소 정보까지 같이), 삭제된 리뷰도 보이게 할건지??
    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDTO<UserReviewDTO> getUserReviewList(Long userId, int size, int page) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));    // 404

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<UserReviewDTO> getUserReviewList = userRepository.findReviewListByUserIdWithPlace(user.getUserId(), pageRequest);


        return new PaginationResponseDTO<>(getUserReviewList.getContent(), getUserReviewList);

    }

    // 모임상세 접근시 사용자 상태 조회(신고여부, 신청여부, 수락여부)
    @Override
    @Transactional(readOnly = true)
    public UserMeetingStatusDTO getUserMeetingStatus(Long userId, Long meetingId) {
        // 신청여부
        boolean isJoined = userMeetingRepository.existsByUser_UserIdAndMeeting_MeetingId(userId, meetingId);
        // 수락여부
        boolean isAccepted = userMeetingRepository.existsByUser_UserIdAndMeeting_MeetingIdAndIsAcceptedTrue(userId, meetingId);
        // 신고여부
        boolean isReported = reportRepository.existsByMeeting_MeetingIdAndReporter_UserId(meetingId, userId);

        return new UserMeetingStatusDTO(userId, meetingId, isReported, isAccepted, isJoined);
    }

    // 승인 대기자 목록(각각 모임에 대한)
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUserMeetingApprovalList(Long meetingId) {
        List<User> getUsers = userRepository.getUserMeetingApprovalList(meetingId);

        return getUsers.stream()
                .map(UserMapper::toAppResponseDTO)
                .collect(Collectors.toList());
    }

    // 닉네임 유효성 검사
    private void validateNickname(String nickname) {
        if (nickname == null || !Pattern.matches("^[가-힣a-zA-Z0-9]{2,15}$", nickname)) {
            throw new CustomException(ExceptionStatus.INVALID_NICKNAME_FORMAT); // 닉네임은 2글자 이상 15글자 이하이며, 한글, 영어, 숫자만 가능합니다.
        }
    }

    // 아이디 유효성 검사
    private void validateLoginId(String loginId) {
        if (loginId == null || !Pattern.matches("^[a-z0-9]{6,12}$", loginId)) {
            throw new CustomException(ExceptionStatus.INVALID_LOGIN_ID_FORMAT); // 아이디는 6글자 이상 12글자 이하이며, 영어 소문자와 숫자만 가능합니다.
        }
    }

    // 비번 유효성 검사
    private void validatePassword(String password) {
        if (password == null ||
                !Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,16}$", password)) {
            throw new CustomException(ExceptionStatus.INVALID_PASSWORD_FORMAT); // 비밀번호는 8글자 이상 16글자 이하이며, 영어와 숫자를 포함해야 합니다.
        }
    }

    // 이메일 유효성 검사
    private void validateEmail(String email) {
        if (email == null ||
                !Pattern.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", email)) {
            throw new CustomException(ExceptionStatus.INVALID_EMAIL_FORMAT); // 유효한 이메일 형식이 아닙니다.
        }
    }

    // 전화번호 유효성 검사
    private void validatePhoneNumber(String phoneNum) {
        if (phoneNum == null || !Pattern.matches("^\\d{10,11}$", phoneNum)) {
            throw new CustomException(ExceptionStatus.INVALID_PHONE_FORMAT); // 전화번호는 10~11자리 숫자여야 합니다.
        }
    }

    // 수정시 중복 검사(자신의 현재 값은 제외)
    private void validateDuplicateFieldsForUpdate(Long userId, UserFormDTO dto) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

        // 전화번호 중복 검사(현재 사용자 제외)
        // 전화번호 값이 비어있지 않고 현재 전화번호랑 입력된 전화번호가 일치하지 않을때
        // 현재 전화번호랑 입력된 전화번호가 일치할때는??? -> 이건 프론트쪽에서 기존값이 변경되었다는 것을 감지할때 보내는 방식으로 하면 이 로직은 굳이??
        if (StringUtils.hasText(dto.getPhoneNum()) && !dto.getPhoneNum().equals(currentUser.getPhoneNum())) {
            // 중복 검사
            if (userRepository.existsByPhoneNum(dto.getPhoneNum())) {
                throw new CustomException(ExceptionStatus.DUPLICATE_PHONE); // 이미 사용 중인 전화번호입니다.
            }
        }

        // 닉네임 중복 검사(현재 사용자 제외)
        if (StringUtils.hasText(dto.getNickname()) && !dto.getNickname().equals(currentUser.getNickname())) {
            if (userRepository.existsByNickname(dto.getNickname())) {
                throw new CustomException(ExceptionStatus.DUPLICATE_NICKNAME); // 이미 사용 중인 닉네임입니다.
            }
        }

    }

    // MeetingStatus가 'DELETED'인 것만 제외하는 메서드
    private List<MeetingStatus> validMeetingStatus() {
        List<MeetingStatus> validStatus = Arrays.stream(MeetingStatus.values())
                .filter(status -> status != MeetingStatus.DELETED)
                .collect(Collectors.toList());

        return validStatus;
    }
}
