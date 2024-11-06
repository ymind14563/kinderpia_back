package sesac_3rd.sesac_3rd.mapper.place;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sesac_3rd.sesac_3rd.dto.place.PlaceDTO;
import sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.entity.PlaceCategory;
import sesac_3rd.sesac_3rd.repository.PlaceCategoryRepository;

@Component
public class PlaceMapper {

    @Autowired
    private static PlaceCategoryRepository placeCategoryRepository;

    // DTO to entity
    public static Place convertToEntity(PlaceDTO placeDTO) {
        // PlaceCategory 객체를 DB에서 조회
        PlaceCategory placeCategory = placeCategoryRepository.findByPlaceCtgName(placeDTO.getPlaceCategoryName());

        // 카테고리가 존재하지 않을 경우 처리 (예: 예외 던지기)
        if (placeCategory == null) {
            throw new RuntimeException("PlaceCategory not found with name: " + placeDTO.getPlaceCategoryName());
        }

        return Place.builder()
                .placeId(placeDTO.getPlaceId())
                .placeCategory(placeCategory) // DB에서 조회한 PlaceCategory 설정
                .placeName(placeDTO.getPlaceName())
                .location(placeDTO.getLocation())
                .detailAddress(placeDTO.getDetailAddress())
                .operatingDate(placeDTO.getOperatingDate())
                .latitude(placeDTO.getLatitude())
                .longitude(placeDTO.getLongitude())
                .placeImg(placeDTO.getPlaceImg())
                .isPaid(placeDTO.isPaid())
                .homepageUrl(placeDTO.getHomepageUrl())
                .placeNum(placeDTO.getPlaceNum())
                .build();
    }

    // Entity to DTO
    public static PlaceDTO convertToDTO(Place place) {
        return PlaceDTO.builder()
                .placeId(place.getPlaceId())
                .placeCategoryName(place.getPlaceCategory().getPlaceCtgName())
                .placeName(place.getPlaceName())
                .location(place.getLocation())
                .detailAddress(place.getDetailAddress())
                .operatingDate(place.getOperatingDate())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .placeImg(place.getPlaceImg())
                .isPaid(place.isPaid())
                .homepageUrl(place.getHomepageUrl())
                .placeNum(place.getPlaceNum())
                .build();
    }
    //  Entity to PlaceReviewDTO
    public PlaceReviewDTO convertToPlaceReviewDTO(Place place) {
        return PlaceReviewDTO.builder()
                .placeId(place.getPlaceId())
                .placeName(place.getPlaceName())
                .location(place.getLocation())
                .detailAddress(place.getDetailAddress())
                .operatingDate(place.getOperatingDate())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .placeImg(place.getPlaceImg())
                .isPaid(place.isPaid())
                .homepageUrl(place.getHomepageUrl())
                .placeNum(place.getPlaceNum())
                .placeCtgName(place.getPlaceCategory().getPlaceCtgName())
                .build();
    }
}