package sesac_3rd.sesac_3rd.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 커스텀 예외 제외한 응답 구조 통일
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseHandler<T> {
    private T data;
    private int status;      // HTTP 상태 코드
    private String message;
}
