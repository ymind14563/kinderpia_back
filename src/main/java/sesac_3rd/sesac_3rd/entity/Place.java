package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Place")
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id", nullable = false)
    private Long placeId;  // 장소아이디 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_ctg_id", nullable = false)  // 외래 키 설정 (FK)
    private PlaceCategory placeCategory;  // 장소카테고리 (다대일 관계)

    @Column(name = "place_name", nullable = false, length = 100)
    private String placeName;  // 장소명

    @Column(name = "location", nullable = false, length = 20)
    private String location;  // 지역구

    @Column(name = "detail_address", nullable = false, length = 100)
    private String detailAddress;  // 상세주소

    @Column(name = "operating_date", nullable = false, length = 30)
    private String operatingDate;  // 운영일자

    @Column(name = "latitude", nullable = false)
    private float latitude;  // 위도

    @Column(name = "longitude", nullable = false)
    private float longitude;  // 경도

    @Column(name = "place_img", nullable = false, length = 300)
    private String placeImg;  // 장소이미지

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid = false;  // 요금정보 (기본값 FALSE)

    @Column(name = "homepage_url", nullable = false, length = 500)
    private String homepageUrl;  // 홈페이지주소

    @Column(name = "place_num", nullable = false, length = 30)
    private String placeNum;  // 전화번호
}
