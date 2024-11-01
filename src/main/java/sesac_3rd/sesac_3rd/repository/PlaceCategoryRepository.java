package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac_3rd.sesac_3rd.entity.PlaceCategory;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory,Long> {
    PlaceCategory findByPlaceCtgName(String placeCategoryName);
}
