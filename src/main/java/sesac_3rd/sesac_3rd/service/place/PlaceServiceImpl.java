package sesac_3rd.sesac_3rd.service.place;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.place.PlaceDTO;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.repository.PlaceRepository;

import java.util.ArrayList;
import java.util.List;

import static sesac_3rd.sesac_3rd.mapper.place.PlaceMapper.convertToDTO;

@Slf4j
@Service
public class PlaceServiceImpl implements PlaceService{

    @Autowired
    private PlaceRepository placeRepository;

    @Override
    public Page<Place> getAllPlace(String sort, int page, int limit, String category, String keyword){

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("placeId"));
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sorts));
        // 메인 페이지 및 정렬
        switch(sort){
            case "date":
                return this.placeRepository.findAll(pageable);
            default:
                throw new IllegalArgumentException("Invalid condition: " + sort);
        }
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
    public PlaceDTO getPlaceById(Long placeId){
        Place place = placeRepository.findById(placeId).orElseThrow(()->new CustomException(ExceptionStatus.PLACE_NOT_FOUND));

        return convertToDTO(place);
    }
}
