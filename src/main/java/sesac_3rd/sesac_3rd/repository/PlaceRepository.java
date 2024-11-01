package sesac_3rd.sesac_3rd.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    // 전체 조회
    @Query(value= "select p.*,pct.place_ctg_name from place p join place_category pct where p.place_ctg_id = pct.place_ctg_id", nativeQuery = true)
    Page<Place> getAllPlaceRepo(Pageable pageable);

    // 검색 - 위치(지역구)
    Page<Place> findByLocationContaining(String keyword, Pageable pageable);
    // 검색 - 장소명
    Page<Place> findByPlaceNameContaining(String keyword, Pageable pageable);

    // 장소 아이디 찾기


}
