package sesac_3rd.sesac_3rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.service.place.PlaceService;

@RestController
@RequestMapping("/api/place")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    // 장소 목록 조회
    @GetMapping
    private ResponseEntity<ResponseHandler<Page<PlaceReviewDTO>>> getAllPlace(
            @RequestParam(value = "page", defaultValue = "0") int page, // 시작 페이지
            @RequestParam(value = "size", defaultValue = "10") int size // 크기
    ) {
        try {
            Page<PlaceReviewDTO> page1 = placeService.getAllPlace(page, size);

            ResponseHandler<Page<PlaceReviewDTO>> response = new ResponseHandler<>(
                    page1,
                    HttpStatus.OK.value(), // 200
                    "장소 목록 조회 완료"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 장소 검색
    @PostMapping
    private Page<PlaceReviewDTO> findByContaining(
            @RequestParam(value = "sort", defaultValue = "default") String sort, // 정렬 순서
            @RequestParam(value = "page", defaultValue = "0") int page, // 시작 페이지
            @RequestParam(value = "size", defaultValue = "10") int size, // 크기
            @RequestParam(value = "category", defaultValue = "all") String category, // 검색 카테고리
            @RequestParam(value = "keyword", defaultValue = "none") String keyword
    ) {
        return placeService.findByContaining(sort, page, size, category, keyword);
    }

    // 장소 상세 조회
    @GetMapping("/{id}")
    private ResponseEntity<ResponseHandler<PlaceReviewDTO>> getPlaceById(@PathVariable(value = "id") Long placeId) {
        try {
            PlaceReviewDTO placeDTO = placeService.getPlaceById(placeId);
            ResponseHandler<PlaceReviewDTO> response = new ResponseHandler<>(
                    placeDTO,
                    HttpStatus.OK.value(),
                    "장소 상세 조회 완료"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
