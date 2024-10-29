package sesac_3rd.sesac_3rd.handler.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@NoArgsConstructor
public class PageInfo {
    private int page = 1;            // 기본값: 1 (현재 페이지 번호, 1부터 시작)
    private long totalElements = 0L; // 기본값: 0 (전체 데이터 수)
    private int totalPages = 1;      // 기본값: 1 (전체 페이지 수)
    private Sort sort = Sort.unsorted(); // 기본값: 정렬 없음 (UNSORTED)

    /*
     * 정렬 종류
     *
     * 1. Sort.unsorted() - 정렬 없음
     * 2. Sort.by("fieldName") - 특정 필드 기준 오름차순 정렬
     * 3. Sort.by(Sort.Order.asc("fieldName")) - 오름차순 정렬
     * 4. Sort.by(Sort.Order.desc("fieldName")) - 내림차순 정렬
     * 5. Sort.by("field1", "field2") - 여러 필드 기준 정렬 // "field1"을 기준으로 정렬하고, 값이 같을 경우 "field2"를 기준으로 정렬
     * 6. Sort.by(Sort.Order.asc("field1"), Sort.Order.desc("field2")) - 복합 정렬
     */


    public PageInfo(int page, long totalElements, int totalPages, Sort sort) {
        this.page = page;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.sort = sort != null ? sort : Sort.unsorted();
    }
}
