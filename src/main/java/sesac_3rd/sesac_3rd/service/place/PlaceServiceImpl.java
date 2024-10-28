package sesac_3rd.sesac_3rd.service.place;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.place.PlaceDTO;
import sesac_3rd.sesac_3rd.entity.Place;
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
    public List<PlaceDTO> getAllPlace(){
        List<Place> place = placeRepository.findAll();
        List<PlaceDTO> placeDTO = new ArrayList<>();

        for(Place p: place){
            PlaceDTO dto = convertToDTO(p);
            placeDTO.add(dto);
        }
        return placeDTO;
    }
}
