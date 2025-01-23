package sesac_3rd.sesac_3rd.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;

import java.util.Collections;

@Component
public class CommonFallbackHandler {

    private static final Logger log = LoggerFactory.getLogger(CommonFallbackHandler.class);

    // PaginationResponseDTO를 처리 (리스트 및 단일 데이터 모두 지원)
    public <T> PaginationResponseDTO<T> handlePaginationFallback(
            Pageable pageable, Throwable throwable, Class<T> dtoClass, boolean isList) {

        log.error("Fallback 실행 - handlePaginationFallback: 요청 처리 실패, 에러 메시지: {}", throwable.getMessage());

        if (isList) {
            // 리스트 데이터를 반환 (빈 리스트와 빈 페이지)
            return new PaginationResponseDTO<>(Collections.emptyList(), Page.empty(pageable));
        } else {
            try {
                // 단일 데이터일 경우 (빈 데이터 생성)
                T emptyData = dtoClass.getDeclaredConstructor().newInstance();
                return new PaginationResponseDTO<>(emptyData, Page.empty());
            } catch (Exception e) {
                throw new RuntimeException("Fallback 인스턴스를 생성할 수 없습니다: " + dtoClass.getName(), e);
            }
        }
    }

    // 단일 데이터 처리
    public <T> T handleSingleResponseFallback(Throwable throwable, Class<T> dtoClass) {
        log.error("Fallback 실행 - handleSingleResponseFallback: 요청 처리 실패, 에러 메시지: {}", throwable.getMessage());

        try {
            // 빈 객체 생성
            return dtoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Fallback 인스턴스를 생성할 수 없습니다: " + dtoClass.getName(), e);
        }
    }

    // Page<T>를 처리
    public <T> Page<T> handlePageFallback(Pageable pageable, Throwable throwable) {
        log.error("Fallback 실행 - handlePageFallback: 요청 처리 실패, 에러 메시지: {}", throwable.getMessage());
        return Page.empty(pageable);
    }
}
