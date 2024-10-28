package sesac_3rd.sesac_3rd.service.place;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.place.PlaceDTO;
import sesac_3rd.sesac_3rd.entity.Place;

@Service
public interface PlaceService {

    // 장소 목록 조회
    Page<Place> getAllPlace(String sort, int page, int limit, String category, String keyword);

    // 장소 검색 (제목, 주소)
    Page<Place> findByContaining(String sort, int page, int limit, String category, String keyword);

    // 장소 상세 조회
    PlaceDTO getPlaceById(Long placeId);

    // 검색 결과 조회

}
