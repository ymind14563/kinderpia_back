package sesac_3rd.sesac_3rd.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO;
import sesac_3rd.sesac_3rd.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    // 장소 아이디로 조회
    Place findByPlaceId(Long placeId);

    // 전체 조회 (이 부분은 올바르게 작성되어 있습니다)
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO(" +
            "p.placeId, p.placeName, p.location, " +
            "p.detailAddress, p.operatingDate, p.latitude, p.longitude, p.placeImg, p.homepageUrl, p.placeNum, p.isPaid, pc.placeCtgName, " +
            "CAST(COALESCE(ROUND(AVG(CASE WHEN r.isDeleted = false THEN r.star ELSE null END)), 0) AS int) AS averageStar, " +
            "CAST(COUNT(CASE WHEN r.isDeleted = false THEN r.id ELSE null END) AS int) AS totalReviewCount) " +
            "FROM Place p " +
            "JOIN p.placeCategory pc " +
            "LEFT JOIN Review r ON p.placeId = r.place.placeId " +
            "GROUP BY p.placeId, p.placeName, p.location, p.detailAddress, p.operatingDate, p.latitude, p.longitude, p.placeImg, p.homepageUrl, p.placeNum, p.isPaid, pc.placeCtgName")
    Page<PlaceReviewDTO> getAllPlace(Pageable pageable);

    // 검색 - 위치(지역구)
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO(" +
            "p.placeId, p.placeName, p.location, " +
            "p.detailAddress, p.operatingDate, p.latitude, p.longitude, p.placeImg, p.homepageUrl, p.placeNum, p.isPaid, pc.placeCtgName, " +
            "CAST(COALESCE(ROUND(AVG(CASE WHEN r.isDeleted = false THEN r.star ELSE null END)), 0) AS int) AS averageStar, " +
            "CAST(COUNT(CASE WHEN r.isDeleted = false THEN r.id ELSE null END) AS int) AS totalReviewCount) " +
            "FROM Place p " +
            "JOIN p.placeCategory pc " +
            "LEFT JOIN Review r ON p.placeId = r.place.placeId " +
            "WHERE p.location LIKE %:keyword% " +
            "GROUP BY p.placeId, p.placeName, p.location, p.detailAddress, p.operatingDate, p.latitude, p.longitude, p.placeImg, p.homepageUrl, p.placeNum, p.isPaid, pc.placeCtgName")
    Page<PlaceReviewDTO> findByLocationContaining(@Param("keyword") String keyword, Pageable pageable);

    // 검색 - 장소명
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO(" +
            "p.placeId, p.placeName, p.location, " +
            "p.detailAddress, p.operatingDate, p.latitude, p.longitude, p.placeImg, p.homepageUrl, p.placeNum, p.isPaid, pc.placeCtgName, " +
            "CAST(COALESCE(ROUND(AVG(CASE WHEN r.isDeleted = false THEN r.star ELSE null END)), 0) AS int) AS averageStar, " +
            "CAST(COUNT(CASE WHEN r.isDeleted = false THEN r.id ELSE null END) AS int) AS totalReviewCount) " +
            "FROM Place p " +
            "JOIN p.placeCategory pc " +
            "LEFT JOIN Review r ON p.placeId = r.place.placeId " +
            "WHERE p.placeName LIKE %:keyword% " +
            "GROUP BY p.placeId, p.placeName, p.location, p.detailAddress, p.operatingDate, p.latitude, p.longitude, p.placeImg, p.homepageUrl, p.placeNum, p.isPaid, pc.placeCtgName")
    Page<PlaceReviewDTO> findByPlaceNameContaining(@Param("keyword") String keyword, Pageable pageable);

    // 검색 - 서울시 전체
    @Query("SELECT new sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO(" +
            "p.placeId, p.placeName, p.location, " +
            "p.detailAddress, p.operatingDate, p.latitude, p.longitude, p.placeImg, p.homepageUrl, p.placeNum, p.isPaid, pc.placeCtgName, " +
            "CAST(COALESCE(ROUND(AVG(CASE WHEN r.isDeleted = false THEN r.star ELSE null END)), 0) AS int) AS averageStar, " +
            "CAST(COUNT(CASE WHEN r.isDeleted = false THEN r.id ELSE null END) AS int) AS totalReviewCount) " +
            "FROM Place p " +
            "JOIN p.placeCategory pc " +
            "LEFT JOIN Review r ON p.placeId = r.place.placeId " +
            "WHERE p.detailAddress LIKE '서울%' " +
            "GROUP BY p.placeId, p.placeName, p.location, p.detailAddress, p.operatingDate, p.latitude, p.longitude, p.placeImg, p.homepageUrl, p.placeNum, p.isPaid, pc.placeCtgName")
    Page<PlaceReviewDTO> findBySeoulContaining(Pageable pageable);

}