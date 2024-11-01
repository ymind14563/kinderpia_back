package sesac_3rd.sesac_3rd.handler.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PageInfo {
    private int page = 1;                 // 기본값: 1  (현재 페이지 번호, 1부터 시작)
    private int pageSize = 10;            // 기본값: 10 (한 페이지에 보여줄 요소 수)
    private long totalElements = 0L;      // 기본값: 0  (전체 데이터 수)
    private int totalPages = 1;           // 기본값: 1  (전체 페이지 수)
    private List<String> sortFields;      // 정렬 필드 리스트
    private List<String> sortDirections;  // 정렬 방향 리스트


    public PageInfo(int page, int pageSize, Long totalElements, int totalPages, Sort sort) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.sortFields = new ArrayList<>();
        this.sortDirections = new ArrayList<>();

        // sort가 존재하지 않거나 정렬되어있지 않거나 필드나 방향이 없거나 (예외처리)
        if (sort == null || !sort.isSorted() || sortFields.isEmpty() || sortDirections.isEmpty()) {
            sort = Sort.by(Sort.Order.asc("id")); // 기본 정렬 설정 (id, asc)

        } else if (sort != null && sort.isSorted()) {
            for (Sort.Order order : sort) { // 모든 정렬 기준 (정렬 기준이 다수일 때 처리를 위함)
                this.sortFields.add(order.getProperty());               // 필드
                this.sortDirections.add(order.getDirection().name());   // 방향
            }
        }
    }
}
