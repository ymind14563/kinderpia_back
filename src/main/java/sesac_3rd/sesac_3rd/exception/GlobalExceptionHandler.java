package sesac_3rd.sesac_3rd.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리를 위한 Handler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * CustomException 처리를 위한 핸들러
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        log.error("handleCustomException", e);
        ExceptionStatus exceptionStatus = e.getExceptionStatus();

        ErrorResponse response = ErrorResponse.builder()
                .exceptionStatus(exceptionStatus)
                .build();

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(exceptionStatus.getStatus()));
    }
}
