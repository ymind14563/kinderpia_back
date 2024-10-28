package sesac_3rd.sesac_3rd.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 에러 응답을 위한 DTO 클래스
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;

    @Builder
    public ErrorResponse(ExceptionStatus exceptionStatus){
        this.message = exceptionStatus.getMessage();
        this.status = exceptionStatus.getStatus();
        this.timestamp = LocalDateTime.now();
    }
}
