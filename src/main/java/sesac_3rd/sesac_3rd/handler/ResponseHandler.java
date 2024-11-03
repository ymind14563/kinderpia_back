package sesac_3rd.sesac_3rd.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;

// 커스텀 예외 제외한 응답 구조 통일
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseHandler<T> {
    private T data;
    private int status;      // HTTP 상태 코드
    private String message;


    // 공통 ResponseHandler 응답 구조 메서드화
    public static <T> ResponseEntity<ResponseHandler<T>> response(T data, HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ResponseHandler<>(data, status.value(), message));
    }

    public static <T> ResponseEntity<ResponseHandler<T>> unauthorizedResponse() {
        return response(null,
                HttpStatus.valueOf(ExceptionStatus.UNAUTHORIZED_REQUEST.getStatus()),
                ExceptionStatus.UNAUTHORIZED_REQUEST.getMessage());
    }

}
