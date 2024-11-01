package sesac_3rd.sesac_3rd.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac_3rd.sesac_3rd.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    // 검색 - 위치(지역구)
    Page<Place> findByLocationContaining(String keyword, Pageable pageable);
    // 검색 - 장소명
    Page<Place> findByPlaceNameContaining(String keyword, Pageable pageable);


}
