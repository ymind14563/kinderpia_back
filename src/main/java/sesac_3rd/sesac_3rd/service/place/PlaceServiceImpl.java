package sesac_3rd.sesac_3rd.service.place;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

import static sesac_3rd.sesac_3rd.mapper.place.PlaceMapper.convertToDTO;

@Slf4j
@Service
public class PlaceServiceImpl implements PlaceService{

    @Autowired
    private PlaceRepository placeRepository;

    @Override
    public List<PlaceDTO> getAllPlace(String sort, int page, int limit){
        List<Sort.Order> sorts = new ArrayList<>();
        switch(sort){
            case "date":
                sorts.add(Sort.Order.desc("placeId"));
                Pageable pageable = PageRequest.of(page, limit, Sort.by(sorts));
                return this.placeRepository.findAll(pageable);
                break;
            default:
                throw new IllegalArgumentException("Invalid condition: " + sort);
        }
    }

    @Override
    public PlaceDTO getPlaceById(Long placeId){
        Place place = placeRepository.findById(placeId).orElseThrow(()-> throw new CustomException(ExceptionStatus.PLACE_NOT_FOUND));

        return convertToDTO(place);
    }
}
