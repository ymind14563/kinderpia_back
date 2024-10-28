package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.dto.place.PlaceDTO;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.service.place.PlaceService;

import java.util.List;

@RestController
@RequestMapping("/api/place")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    // 장소 목록 조회
    @GetMapping
    private Page<Place> getAllPlace(
            @RequestParam(defaultValue = "date") String sort, //정렬 순서
            @RequestParam(defaultValue = "0") int page, //시작 페이지
            @RequestParam(defaultValue = "10") int limit, //크기
            @RequestParam(defaultValue = "all") String category, //검색 카테고리
            @RequestParam(defaultValue = "none") String keyword
    ) { // 검색 키워드
        return placeService.getAllPlace(sort, page, limit, category, keyword);
    }

    // 장소 검색
    @PostMapping
    private Page<Place> findByContaining(
            @RequestParam(defaultValue = "date") String sort, //정렬 순서
            @RequestParam(defaultValue = "0") int page, //시작 페이지
            @RequestParam(defaultValue = "10") int limit, //크기
            @RequestParam(defaultValue = "all") String category, //검색 카테고리
            @RequestParam(defaultValue = "none") String keyword
    ){
        return placeService.findByContaining(sort, page, limit, category, keyword);
    }

    // 장소 상세 조회
    @GetMapping("/{id}")
    private PlaceDTO getPlaceById(@PathVariable long placeId){
        return placeService.getPlaceById(placeId);
    }

    // 장소 검색


}
