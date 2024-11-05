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
import sesac_3rd.sesac_3rd.dto.place.PlaceWithCategoryDTO;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.entity.PlaceCategory;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.mapper.place.PlaceMapper;
import sesac_3rd.sesac_3rd.repository.PlaceCategoryRepository;
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
    private PlaceCategoryRepository placeCategoryRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PlaceMapper placeMapper;

    // 장소 목록 조회
//    @Override
//    public Page<PlaceReviewDTO> getAllPlace(int page, int size){
////        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "placeId"));
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC));
//        Page<PlaceReviewDTO> result = placeRepository.getAllPlace(pageable);
//        return result;
//
//    }

    @Override
    public PlaceReviewDTO TestgetAllPlace(){
        PlaceWithCategoryDTO result = placeRepository.getAllPlace();
        System.out.println("result >> " + result);
        PlaceReviewDTO placeReviewDTO = null;
        return placeReviewDTO;

    }

    // 검색 결과
    @Override
    public Page<PlaceWithCategoryDTO> findByContaining(String sort, int page, int size, String category, String keyword){
    List<Sort.Order> resultPlace = new ArrayList<>();
    resultPlace.add(Sort.Order.desc("placeId"));
    Pageable pageable = PageRequest.of(page, size, Sort.by(resultPlace));
    System.out.println("category : "  + category.getClass().getName());
    System.out.println("category value: " + category);

    if (category.equals("title")) {
        return placeRepository.findByPlaceNameContaining(keyword, pageable);
    }
    else if (category.equals("address")) {
        return placeRepository.findByLocationContaining(keyword, pageable);
    }
    else{
        throw new CustomException(ExceptionStatus.CATEGORY_NOT_FOUND);
    }
}

    @Override
    public PlaceReviewDTO getPlaceById(Long placeId){
        Place place = placeRepository.findById(placeId).orElseThrow(()->new CustomException(ExceptionStatus.PLACE_NOT_FOUND));
        String placeCtgName = place.getPlaceCategory().getPlaceCtgName();
        // 장소별 평균 평점 조회
        Integer avgStar = reviewRepository.getAverageStar(placeId);
        System.out.println("avgStar : " + avgStar);
        PlaceReviewDTO placeReviewDTO = placeMapper.convertToPlaceReviewDTO(place);
        placeReviewDTO.setAverageStar(avgStar);

        return placeReviewDTO;
    }
}
