package sesac_3rd.sesac_3rd.exception;

// 커스텀 에러
public enum ExceptionStatus {
    // Common

    // User
    // User Registration
    INVALID_NICKNAME_FORMAT(400, "닉네임은 2글자 이상 15글자 이하이며, 한글, 영어, 숫자만 가능합니다."),
    INVALID_LOGIN_ID_FORMAT(400, "아이디는 6글자 이상 12글자 이하이며, 영어 소문자와 숫자만 가능합니다."),
    INVALID_PASSWORD_FORMAT(400, "비밀번호는 8글자 이상 16글자 이하이며, 영어와 숫자를 포함해야 하고 특수문자 가능합니다."),
    INVALID_EMAIL_FORMAT(400, "유효한 이메일 형식이 아닙니다."),
    INVALID_PHONE_FORMAT(400, "전화번호는 10~11자리 숫자여야 합니다."),
    INVALID_PASSWORD(401, "아이디 또는 비밀번호를 찾을 수 없습니다."),
    WITHDRAWN_USER(403, "탈퇴한 사용자입니다."),
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(409, "이미 사용 중인 이메일입니다."),
    DUPLICATE_PHONE(409, "이미 사용 중인 전화번호입니다."),
    DUPLICATE_LOGIN_ID(409, "이미 사용 중인 아이디입니다."),
    DUPLICATE_NICKNAME(409, "이미 사용 중인 닉네임입니다."),


    //Place
    PLACE_NOT_FOUND(404, "장소 정보를 찾을 수 없습니다."),
    PLACEID_NOT_FOUND(404,"장소아이디를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(404,"카테고리 명이 일치하지 않습니다."),

    //Review
    REVIEWID_NOT_FOUND(404,"리뷰아이디를 찾을 수 없습니다."),
    REVIEW_ALREADY_WRITE(409, "해당 장소에 대한 리뷰는 이미 작성하였습니다."),
    USER_NOT_WRITER(404,"로그인 유저와 작성자가 일치하지 않습니다."),

    // Meeting
    MEETING_NOT_FOUND(404, "모임을 찾을 수 없습니다."),
    MEETING_CAPACITY_FULL(409, "모임의 총 인원이 다 찼습니다."),
    USER_NOT_READER(404, "모임장이 아닙니다."),

    // UserMeeting
    MEETING_ALREADY_JOINED(409, "이미 참가한 모임입니다."),
    MEETING_NOT_JOINED(403, "참가중인 모임이 아닙니다."),

    // Chat
    CHATROOM_NOT_FOUND(404, "채팅방을 찾을 수 없습니다."),
    USER_NOT_IN_CHATROOM(403, "채팅방에 소속된 회원이 아닙니다."),
    CHATROOM_NOT_ACTIVE(404, "활성화된 채팅방이 아닙니다."),
    INVALID_MESSAGE_TYPE(400,"유효한 메세지 타입이 아닙니다."),
    CHATMSG_NOT_FOUND(404, "채팅 메세지를 찾을 수 없습니다."),
    CHATMSG_USER_NOT_MATCH(403,"발신자 정보와 토큰이 일치하지 않습니다."),


    // Report
    DUPLICATE_REPORT(409, "이미 유효한 신고입니다."),
    REPORT_RS_NOT_FOUND(404, "존재하지 않는 신고유형입니다."),
    INVALID_REPORT_TARGET(400, "신고에 대상이 없습니다."),
    NO_REPORT_FOUND(404,"신고 내역이 존재하지 않습니다."),

    // Manager
    MANAGER_NOT_FOUND(404, "관리자를 찾을 수 없습니다."),


    // Authorization
    UNAUTHORIZED_REQUEST(401, "유효하지 않은 토큰입니다."),

    // S3
    EMPTY_FILE(400, "업로드된 파일이 비어 있습니다. 유효한 파일을 업로드해 주세요."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(500, "이미지 업로드 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."),
    PUT_OBJECT_EXCEPTION(500, "PUT OBJECT ERROR"),
    IO_EXCEPTION_ON_IMAGE_DELETE(500, "이미지 삭제 중 오류가 발생했습니다.");


    private final int status;
    private final String message;

    ExceptionStatus(int status, String message){
        this.status = status;
        this.message = message;
    }

    // getter
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
