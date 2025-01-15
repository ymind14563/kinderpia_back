package sesac_3rd.sesac_3rd.service.place;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.place.PlaceDTO;
import sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO;
import sesac_3rd.sesac_3rd.dto.place.PlaceWithCategoryDTO;
import sesac_3rd.sesac_3rd.dto.place.PopularPlaceDTO;
import sesac_3rd.sesac_3rd.entity.Place;
import sesac_3rd.sesac_3rd.entity.PlaceCategory;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.mapper.place.PlaceMapper;
import sesac_3rd.sesac_3rd.repository.PlaceCategoryRepository;
import sesac_3rd.sesac_3rd.repository.PlaceRepository;
import sesac_3rd.sesac_3rd.repository.ReviewRepository;

import static sesac_3rd.sesac_3rd.mapper.place.PlaceMapper.convertToDTO;

import java.time.Duration;
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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private static final String POPULAR_PLACES_KEY = "popular_places";

    public PlaceServiceImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }
    // 장소 목록 조회
    @Override
    public Page<PlaceReviewDTO> getAllPlace(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "averageStar"));
        Page<PlaceReviewDTO> result = placeRepository.getAllPlace(pageable);


        return result;

    }

    // 검색 결과
    @Override
    public Page<PlaceReviewDTO> findByContaining(String sort, int page, int size, String category, String keyword){

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("placeId")));
        System.out.println("category : "  + category.getClass().getName());
        System.out.println("category value: " + category);
        System.out.println("sort : " + sort);
        if(sort.equals("star")){
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("averageStar")));
        }
        if (category.equals("title")) {
            return placeRepository.findByPlaceNameContaining(keyword, pageable);
        }
        else if (category.equals("address")) {
            if(keyword.equals("seoul")){
                System.out.println("category addr / keyword : seoul ");
                return placeRepository.findBySeoulContaining(pageable);
            }
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

    // 인기 장소 조회 (redis key-value 방식을 활용, 따로 RedisRepository 없어도 됨)
    @Override
    public List<PopularPlaceDTO> getPopularPlaces() {
        try {
            // Redis에서 JSON 데이터를 가져오기
            String cachedData = (String) redisTemplate.opsForValue().get(POPULAR_PLACES_KEY);
            System.out.println("Redis에서 조회 완료");
            List<PopularPlaceDTO> popularPlaces;
            if (cachedData == null || cachedData.isEmpty()) {
                // Redis에 데이터가 없으면 DB에서 가져오기 (상위 8개)
                Pageable pageable = PageRequest.of(0, 8);
                popularPlaces = placeRepository.getTop8PopularPlaces(pageable);
                System.out.println("DB에서 조회 완료");
                savePopularPlacesToRedis(popularPlaces);
            } else {
                // JSON 문자열을 List<PopularPlaceDTO>로 역직렬화
                popularPlaces = objectMapper.readValue(cachedData, new TypeReference<List<PopularPlaceDTO>>() {});
            }

            return popularPlaces;
        } catch (Exception e) {
            throw new RuntimeException("인기 장소 조회 중 오류 발생", e);
        }
    }


//    // DB에서 인기장소 직접 조회
//    @Override
//    public List<PopularPlaceDTO> getPopularPlaces() {
//        try {
//            Pageable pageable = PageRequest.of(0, 8);
//            List<PopularPlaceDTO> popularPlaces = placeRepository.getTop8PopularPlaces(pageable);
//            System.out.println("DB에서 조회 완료");
//
//            return popularPlaces;
//        } catch (Exception e) {
//            throw new RuntimeException("인기 장소 조회 중 오류 발생", e);
//        }
//    }


    // Redis에 인기 장소 저장
    public void savePopularPlacesToRedis(List<PopularPlaceDTO> popularPlaces) {
        try {
            // List를 JSON 문자열로 직렬화
            String serializedData = objectMapper.writeValueAsString(popularPlaces);

            // Redis에 저장 (opsForValue().set)
            redisTemplate.opsForValue().set(POPULAR_PLACES_KEY, serializedData, Duration.ofDays(1));
            System.out.println("Redis에 저장 완료");
        } catch (Exception e) {
            throw new RuntimeException("Redis에 인기 장소 저장 중 오류 발생", e);
        }
    }
}
