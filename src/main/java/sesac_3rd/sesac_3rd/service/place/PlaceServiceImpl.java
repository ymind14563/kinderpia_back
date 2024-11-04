package sesac_3rd.sesac_3rd.service.place;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.place.PlaceDTO;
import sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.mapper.place.PlaceMapper;
import sesac_3rd.sesac_3rd.repository.PlaceRepository;
import sesac_3rd.sesac_3rd.repository.ReviewRepository;

import static sesac_3rd.sesac_3rd.mapper.place.PlaceMapper.convertToDTO;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PlaceServiceImpl implements PlaceService{

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Page<PlaceDTO> getAllPlace(int page, int limit){
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "placeId"));
        Page<Place> places = placeRepository.getAllPlaceRepo(pageable); // Place 엔티티 페이지 조회

        return places.map(PlaceMapper::convertToDTO); // 인스턴스 메소드로 변환
    }
    @Override
    public Page<Place> findByContaining(String sort, int page, int limit, String category, String keyword){
    // 검색 결과
    List<Sort.Order> resultPlace = new ArrayList<>();
    resultPlace.add(Sort.Order.desc("placeId"));
    Pageable pageable = PageRequest.of(page, limit, Sort.by(resultPlace));
    System.out.println("category : "  + category);
    switch (category){
        case "title":
            return placeRepository.findByPlaceNameContaining(keyword, pageable);
        case "address":
            return placeRepository.findByLocationContaining(keyword, pageable);
        default:
            return this.placeRepository.findAll(pageable);
    }
}

    @Override
    public PlaceReviewDTO getPlaceById(Long placeId){
        Place place = placeRepository.findById(placeId).orElseThrow(()->new CustomException(ExceptionStatus.PLACE_NOT_FOUND));

        // 장소별 평균 평점 조회
        Integer avgStar = reviewRepository.getAverageStar(placeId);
        System.out.println("avgStar : " + avgStar);

        return new PlaceReviewDTO(place,avgStar);
    }
}


//}
