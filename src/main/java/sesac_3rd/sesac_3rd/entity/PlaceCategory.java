package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PlaceCategory")
@Entity
public class PlaceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_ctg_id", nullable = false)
    private Long placeCtgId;  // 장소카테고리아이디 (PK)

    @Column(name = "place_ctg_name", nullable = false, length = 20, unique = true)
    private String placeCtgName;  // 장소카테고리명
}
