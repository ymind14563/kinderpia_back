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

    //Review
    REVIEWID_NOT_FOUND(404,"리뷰아이디를 찾을 수 없습니다."),

    // Meeting
    MEETING_NOT_FOUND(404, "모임을 찾을 수 없습니다."),

    // Chat
    CHATROOM_NOT_FOUND(404, "채팅방을 찾을 수 없습니다."),
    USER_NOT_IN_CHATROOM(404, "채팅방에 소속된 회원이 아닙니다."),
    CHATROOM_NOT_ACTIVE(404, "활성화된 채팅방이 아닙니다."),
    INVALID_MESSAGE_TYPE(400,"유효한 메세지 타입이 아닙니다."),
    CHATMSG_NOT_FOUND(404, "채팅 메세지를 찾을 수 없습니다."),


    // Report
    DUPLICATE_REPORT(409, "이미 유효한 신고입니다."),
    REPORTRS_NOT_FOUND(404, "존재하지 않는 신고유형입니다.");


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
