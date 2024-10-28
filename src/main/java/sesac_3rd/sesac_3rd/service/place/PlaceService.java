package sesac_3rd.sesac_3rd.service.place;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.place.PlaceDTO;
import sesac_3rd.sesac_3rd.repository.PlaceRepository;

import java.util.List;

@Service
public interface PlaceService {

    // 장소 목록 조회
    List<PlaceDTO> getAllPlace();

    PlaceDTO getPlaceById(Long placeId);
}
