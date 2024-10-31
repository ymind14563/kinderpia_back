package sesac_3rd.sesac_3rd.handler.pagination;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
public class PaginationResponseDTO<T> {
    private List<T> dataList;
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
