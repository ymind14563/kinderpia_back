package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sesac_3rd.sesac_3rd.dto.place.PlaceDTO;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.service.place.PlaceService;

import java.util.List;

@RestController
@RequestMapping("/place")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    // 장소 목록 조회
    @GetMapping
    private List<PlaceDTO> getAllPlace() {
        return placeService.getAllPlace();
    }

    // 장소 상세 조회
//    @GetMapping("/{id}")
//    private PlaceDTO getPlaceById(@PathVariable long placeId){
//        return placeService.getPlaceById(placeId);
//    }

}
