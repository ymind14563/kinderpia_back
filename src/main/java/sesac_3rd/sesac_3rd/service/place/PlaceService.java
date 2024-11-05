package sesac_3rd.sesac_3rd.service.place;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO;
import sesac_3rd.sesac_3rd.dto.place.PlaceWithCategoryDTO;
import sesac_3rd.sesac_3rd.entity.Place;

@Service
public interface PlaceService {

    // 장소 목록 조회
    Page<PlaceReviewDTO> getAllPlace(int page, int limit);

    // 장소 검색 (제목, 주소)
    Page<PlaceReviewDTO> findByContaining(String sort, int page, int size, String category, String keyword);

    // 장소 상세 조회
    PlaceReviewDTO getPlaceById(Long placeId);

}
