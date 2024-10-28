package sesac_3rd.sesac_3rd.exception;

/**
 * 사용자 정의 예외를 처리하기 위한 커스텀 예외 클래스
 * RuntimeException을 상속받아 Unchecked Exception으로 구현
 */
public class CustomException extends RuntimeException{
    /** 발생한 에러의 상세 정보를 담고 있는 ExceptionStatus 객체 */
    private final ExceptionStatus exceptionStatus;

    /**
     * ErrorCode만을 파라미터로 받는 생성자
     * ErrorCode에 정의된 기본 메시지를 사용
     *
     * @param exceptionStatus 발생한 에러에 대한 ErrorCode enum 객체
     */
    public CustomException(ExceptionStatus exceptionStatus){
        super(exceptionStatus.getMessage());    // 부모 클래스의 생성자를 호출하여 기본 에러 메시지 설정
        this.exceptionStatus = exceptionStatus;
    }

    /**
     * ExceptionStatus와 커스텀 메시지를 파라미터로 받는 생성자
     * 기본 메시지 대신 사용자가 직접 지정한 메시지를 사용
     *
     * @param exceptionStatus 발생한 에러에 대한 ExceptionStatus enum 객체
     * @param message 사용자가 직접 지정한 에러 메시지
     */
    public CustomException(ExceptionStatus exceptionStatus, String message){
        super(message);   // 부모 클래스의 생성자를 호출하여 커스텀 에러 메시지 설정
        this.exceptionStatus = exceptionStatus;
    }

    /**
     * 현재 예외의 ExceptionStatus를 반환하는 getter 메서드
     *
     * @return 이 예외와 관련된 ExceptionStatus enum 객체
     */
    public ExceptionStatus getExceptionStatus(){
        return exceptionStatus;
    }
}
