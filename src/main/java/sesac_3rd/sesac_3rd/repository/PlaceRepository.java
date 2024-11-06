package sesac_3rd.sesac_3rd.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.dto.place.PlaceWithCategoryDTO;
import sesac_3rd.sesac_3rd.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    // 장소 아이디로 조회
    Place findByPlaceId(Long placeId);

    // 전체 조회
    @Query("SELECT p FROM Place p JOIN FETCH p.placeCategory") // Fetch join을 사용하여 카테고리도 함께 가져옴
    Page<Place> getAllPlaceRepo(Pageable pageable);

    // 검색 - 위치(지역구)
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO(" +
            "p.placeId, p.placeName, p.location, " +
            "p.detailAddress, p.operatingDate, p.latitude, p.longitude, p.placeImg, p.homepageUrl, p.placeNum, p.isPaid, pc.placeCtgName, CAST(COALESCE(ROUND(AVG(r.star)), 0) AS int) AS averageStar) " +
            "FROM Place p " +
            "JOIN p.placeCategory pc " +
            "LEFT JOIN Review r ON p.placeId = r.place.placeId " +
            "WHERE p.location LIKE %:keyword% " +
            "GROUP BY p.placeId, p.placeName, p.location, p.detailAddress, p.operatingDate, p.latitude, p.longitude, p.placeImg, p.homepageUrl, p.placeNum, p.isPaid, pc.placeCtgName ")
    Page<PlaceReviewDTO> findByLocationContaining(@Param("keyword") String keyword, Pageable pageable);

    // 검색 - 장소명
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.place.PlaceWithCategoryDTO(p.placeId, pc.placeCtgName, p.placeName, p.location, " +
            "p.detailAddress, p.operatingDate, p.latitude, p.longitude, p.placeImg, p.homepageUrl, p.placeNum, p.isPaid) " +
            "FROM Place p JOIN p.placeCategory pc WHERE p.placeName LIKE %:keyword%")
    Page<PlaceWithCategoryDTO> findByPlaceNameContaining(@Param("keyword") String keyword, Pageable pageable);

}
