package sesac_3rd.sesac_3rd.handler.pagination;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.fasterxml.jackson.annotation.JsonInclude;


import java.util.List;

@Getter
public class PaginationResponseDTO<T> {
    // 현재 오버로딩하더라도 모든 필드(dataList, data, pageInfo)는 객체가 생성될 때 포함.
    // PaginationResponseDTO 가 List 나 data 방식 하나만 쓰더라도 나머지 하나가 null 로 표시 되는 문제
    // @JsonInclude(JsonInclude.Include.NON_NULL) 사용으로 null 포함되지 않도록 함

    @JsonInclude(JsonInclude.Include.NON_NULL) // null 포함되지 않도록 함
    private List<T> dataList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    private PageInfo pageInfo;


    // 리스트 데이터용 생성자
    public PaginationResponseDTO(List<T> dataList, Page page) {
        this.dataList = dataList;
        this.pageInfo = createPageInfo(page);
    }

    // 단일 데이터용 생성자 (ex.DTO)
    public PaginationResponseDTO(T data, Page page) {
        this.data = data;
        this.pageInfo = createPageInfo(page);
    }

    private PageInfo createPageInfo(Page page) {
        return new PageInfo(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSort());
    }
}
