package sesac_3rd.sesac_3rd.mapper.place;

import sesac_3rd.sesac_3rd.dto.place.PlaceDTO;
import sesac_3rd.sesac_3rd.entity.Place;

public class PlaceMapper {
    // dto to entity
    public Place convertToEntity(PlaceDTO placeDTO){
        return Place.builder()
                .placeId(placeDTO.getPlaceId())
                .placeCategory(placeDTO.getPlaceCategory())
                .placeName(placeDTO.getPlaceName())
                .location(placeDTO.getLocation())
                .detailAddress(placeDTO.getDetailAddress())
                .operatingDate(placeDTO.getOperatingDate())
                .latitude(placeDTO.getLatitude())
                .longitude(placeDTO.getLongtitude())
                .placeImg(placeDTO.getPlaceImg())
                .isPaid(placeDTO.isPaid())
                .homepageUrl(placeDTO.getHomepageUrl())
                .placeNum(placeDTO.getPlaceNum())
                .build();
    }
    // entity to dto
    public static PlaceDTO convertToDTO(Place place) {
        return PlaceDTO.builder()
                .placeId(place.getPlaceId())
                .placeCategory(place.getPlaceCategory())
                .placeName(place.getPlaceName())
                .location(place.getLocation())
                .detailAddress(place.getDetailAddress())
                .operatingDate(place.getOperatingDate())
                .latitude(place.getLatitude())
                .longtitude(place.getLongitude())
                .placeImg(place.getPlaceImg())
                .isPaid(place.isPaid())
                .homepageUrl(place.getHomepageUrl())
                .placeNum(place.getPlaceNum())
                .build();
    }
}
