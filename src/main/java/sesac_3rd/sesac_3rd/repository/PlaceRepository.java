package sesac_3rd.sesac_3rd.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sesac_3rd.sesac_3rd.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Page<Place> findByLocationContaining(String keyword, Pageable pageable);
    Page<Place> findByPlaceNameContaining(String keyword, Pageable pageable);


}
