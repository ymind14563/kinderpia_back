package sesac_3rd.sesac_3rd.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.dto.place.PlaceReviewDTO;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;

import java.util.Collections;
import java.util.List;

@Component
public class CommonFallbackHandler {

    private static final Logger log = LoggerFactory.getLogger(CommonFallbackHandler.class);

    // PaginationResponseDTO를 처리 (리스트 및 단일 데이터 모두 지원)
    public <T> PaginationResponseDTO<T> handlePaginationFallback(
            Pageable pageable, Throwable throwable, Class<T> dtoClass, boolean isList) {

        log.error("Fallback 실행 - handlePaginationFallback: 요청 처리 실패, 에러 메시지: {}", throwable.getMessage());

        if (isList) {
            // 기본 Fallback 데이터 1개 포함된 리스트 반환
            List<T> fallbackList = List.of(createFallbackInstance(dtoClass));
            return new PaginationResponseDTO<>(fallbackList, new PageImpl<>(fallbackList, pageable, 1));
        } else {
            // 단일 데이터 반환
            T fallbackData = createFallbackInstance(dtoClass);
            return new PaginationResponseDTO<>(fallbackData, new PageImpl<>(List.of(fallbackData), pageable, 1));
        }
    }

    // 단일 데이터 처리
    public <T> T handleSingleResponseFallback(Throwable throwable, Class<T> dtoClass) {
        log.error("Fallback 실행 - handleSingleResponseFallback: 요청 처리 실패, 에러 메시지: {}", throwable.getMessage());

        return createFallbackInstance(dtoClass);
    }

    // Page 처리 (Fallback 데이터 1개 포함)
    public <T> Page<T> handlePageFallback(Pageable pageable, Throwable throwable, Class<T> dtoClass) {
        log.error("Fallback 실행 - handlePageFallback: 요청 처리 실패, 에러 메시지: {}", throwable.getMessage());

        List<T> fallbackList = List.of(createFallbackInstance(dtoClass));
        return new PageImpl<>(fallbackList, pageable, 1);
    }

    // Fallback용 DTO 생성 및 기본 메시지 설정
    private <T> T createFallbackInstance(Class<T> dtoClass) {
        try {
            if (dtoClass.equals(PlaceReviewDTO.class)) {
                return dtoClass.cast(PlaceReviewDTO.builder()
                        .placeId(-1L)
                        .placeName("Fallback Place")
                        .location("서버 문제로 인해 정보를 불러올 수 없습니다.")
                        .detailAddress("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                        .latitude(null)
                        .longitude(null)
                        .placeImg(null)
                        .homepageUrl(null)
                        .placeNum(null)
                        .isPaid(false)
                        .placeCtgName("알 수 없음")
                        .averageStar(0)
                        .totalReviewCount(0)
                        .build());
            } else if (dtoClass.equals(MeetingDetailDTO.class)) {
                return dtoClass.cast(MeetingDetailDTO.builder()
                        .meetingId(-1L)
                        .userId(-1L)
                        .profileImg(null)
                        .chatroomId(-1L)
                        .nickname("알 수 없음")
                        .meetingCategory("알 수 없음")
                        .meetingTitle("서버 문제로 인해 정보를 불러올 수 없습니다.")
                        .capacity(0)
                        .totalCapacity(0)
                        .meetingContent("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                        .meetingLocation("알 수 없음")
                        .detailAddress("알 수 없음")
                        .isAuthType(false)
                        .meetingTime(null)
                        .meetingStatus(null)
                        .createdAt(null)
                        .build());
            } else if (dtoClass.equals(MeetingDTO.class)) {
                return dtoClass.cast(MeetingDTO.builder()
                        .meetingId(-1L)
                        .nickname("알 수 없음")
                        .profileImg(null)
                        .totalCapacity(0)
                        .district("알 수 없음")
                        .detailAddress("알 수 없음")
                        .meetingCategory("알 수 없음")
                        .meetingTitle("서버 문제로 인해 정보를 불러올 수 없습니다.")
                        .meetingLocation("알 수 없음")
                        .meetingTime(null)
                        .capacity(0)
                        .meetingStatus(null)
                        .createdAt(null)
                        .build());
            } else {
                return dtoClass.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException("Fallback 인스턴스를 생성할 수 없습니다: " + dtoClass.getName(), e);
        }
    }
}
