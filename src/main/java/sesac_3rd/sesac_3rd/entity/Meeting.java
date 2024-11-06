package sesac_3rd.sesac_3rd.entity;

import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Meeting")
@Entity
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;  // 모임아이디 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // 유저아이디 (외래 키)
    private User user;  // 모임의 주최자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = true)  // 장소아이디 (외래 키, NULL 허용)
    private Place place;  // 장소 (다대일 관계)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_ctg_id", nullable = false)  // 모임카테고리아이디 (외래 키)
    private MeetingCategory meetingCategory;  // 모임 카테고리 (다대일 관계)

    @Column(name = "meeting_title", nullable = false, length = 100)
    private String meetingTitle;  // 모임명

    @Column(name = "meeting_content", nullable = false, length = 500)
    private String meetingContent;  // 모임내용

    @Column(name = "total_capacity", nullable = false)
    private int totalCapacity;  // 총원 (최대 99)

    @Column(name = "is_limited", nullable = false)
    private Boolean isLimited = false;  // 총원제한여부 (기본값 FALSE)

    @Column(name = "is_authtype", nullable = false)
    private boolean isAuthType = false;  // 인증여부 (기본값 FALSE)

    @Column(name = "capacity", nullable = false)
    @Builder.Default
    private int capacity = 1;  // 참가인원 (기본값 1)

    @Column(name = "meeting_location", nullable = true, length = 100)
    private String meetingLocation;  // 모임장소

    @Column(name = "district", nullable = true, length = 20)
    private String district; // 지역구

    @Column(name = "detail_address", nullable = true, length = 100)
    private String detailAddress; // 상세주소

    @Column(name = "latitude", nullable = true, precision = 10, scale = 8)
    private BigDecimal latitude;  // 위도 (DECIMAL(10, 8))

    @Column(name = "longitude", nullable = true, precision = 11, scale = 8)
    private BigDecimal longitude;  // 경도 (DECIMAL(11, 8))

    @Column(name = "meeting_time", nullable = false)
    private LocalDateTime meetingTime;  // 모임일시

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_status", nullable = false, length = 20)
    private MeetingStatus meetingStatus = MeetingStatus.ONGOING;  // 모임상태 (기본값 "ONGOING")

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;  // 생성일자

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;  // 수정일자

}
