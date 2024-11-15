package sesac_3rd.sesac_3rd.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.config.security.TokenProvider;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingFormDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingStatusDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.service.meeting.MeetingService;

@Slf4j
@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    // 모임 목록 (default - 최신순 정렬)
    @GetMapping("/list")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<MeetingDTO>>> getAllMeetings(
            @RequestParam(value = "page", defaultValue = "1") int page, // 기본 페이지는 1
            @RequestParam(value = "size", defaultValue = "6") int size // 기본 크기는 6
    ) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            PaginationResponseDTO<MeetingDTO> paginationResponse = meetingService.getAllMeetings(pageable);

            // ResponseHandler 생성 및 반환
            ResponseHandler<PaginationResponseDTO<MeetingDTO>> response = new ResponseHandler<>(
                    paginationResponse,
                    HttpStatus.OK.value(), // 200
                    "모임 목록 조회[전체] 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 모임 목록 (open - 열려있는 것만 정렬 + 모임 시간순으로 정렬)
    @GetMapping("/list/open")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<MeetingDTO>>> getOpenMeetings(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "6") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        PaginationResponseDTO<MeetingDTO> paginationResponse = meetingService.getOpenMeetings(pageable);

        return ResponseEntity.ok(
                new ResponseHandler<>(paginationResponse, HttpStatus.OK.value(), "모임 목록 조회[open] 완료")
        );
    }

    // 키워드로 모임 검색 ( 모임 시간순으로 정렬)
    @GetMapping("/list/search")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<MeetingDTO>>> getSearchMeetingsByKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "6") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        PaginationResponseDTO<MeetingDTO> paginationResponse = meetingService.searchMeetingsByKeyword(keyword, pageable);

        return ResponseEntity.ok(
                new ResponseHandler<>(paginationResponse, HttpStatus.OK.value(), "모임 목록 조회[keyword] 완료")
        );
    }

    // 모임 상세조회 (profile_img, chatroom_id 포함)
    @GetMapping("/{meetingId}")
    public ResponseEntity<ResponseHandler<MeetingDetailDTO>> getMeetingDetail(@PathVariable("meetingId") Long meetingId) {
        try {
            MeetingDetailDTO meetingDetailDTO = meetingService.getDetailMeeting(meetingId);

            ResponseHandler<MeetingDetailDTO> response = new ResponseHandler<>(
                    meetingDetailDTO,
                    HttpStatus.OK.value(), // 200
                    "모임 상세 조회 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("getMeetingDetail 호출 중 예외 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 모임 생성
    @PostMapping
    public ResponseEntity<ResponseHandler<Long>> createMeeting(
            @AuthenticationPrincipal Long userId,
            @RequestBody MeetingFormDTO meetingFormDTO) {
        try {
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId 토큰없음 >> " + userId);
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId 토큰있음 >> " + userId);

            Meeting meeting = meetingService.createMeeting(userId, meetingFormDTO);
            log.info("createMeeting 결과: meetingId {}", meeting.getMeetingId());

            ResponseHandler<Long> response = new ResponseHandler<>(
                    meeting.getMeetingId(), // 생성된 meetingId 포함
                    HttpStatus.CREATED.value(), // 200
                    "모임 생성 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("createMeeting 호출 중 예외 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 모임 수정
    @PutMapping("/{meetingId}")
    public ResponseEntity<ResponseHandler<Boolean>> updateMeeting(
            @AuthenticationPrincipal Long userId,
            @PathVariable("meetingId") Long meetingId,
            @RequestBody MeetingFormDTO dto) {
        try {
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId 토큰없음 >> " + userId);
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId 토큰있음 >> " + userId);

            Boolean result = meetingService.updateMeeting(userId, meetingId, dto);
            log.info("updateMeeting 결과: {}", result);

            ResponseHandler<Boolean> response = new ResponseHandler<>(
                    result,
                    HttpStatus.OK.value(), // 200
                    "모임 수정 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("updateMeeting 호출 중 예외 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 모임삭제 (모임장이 삭제, 관리자가 삭제 : DELETED)
    @PutMapping("/{meetingId}/deleted")
    public ResponseEntity<ResponseHandler<Boolean>> deletedMeeting(
            @AuthenticationPrincipal Long userId,
            @PathVariable("meetingId") Long meetingId) {
        try {
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId 토큰없음 >> " + userId);
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId 토큰있음 >> " + userId);

            Boolean result = meetingService.deletedMeeting(userId, meetingId);
            log.info("deletedMeeting 결과: {}", result);

            ResponseHandler<Boolean> response = new ResponseHandler<>(
                    result, // true
                    HttpStatus.OK.value(), // 200
                    "모임삭제 (DELETED)"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("deletedMeeting 호출 중 예외 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 모집완료 (인원마감, 모임장이 임의로 마감 : COMPLETED)
    @PutMapping("/{meetingId}/completed")
    public ResponseEntity<ResponseHandler<Boolean>> completedMeeting(
            @AuthenticationPrincipal Long userId,
            @PathVariable("meetingId") Long meetingId) {
        try {
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId 토큰없음 >> " + userId);
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId 토큰있음 >> " + userId);

            Boolean result = meetingService.completedMeeting(userId, meetingId);
            log.info("completedMeeting 결과: {}", result);

            ResponseHandler<Boolean> response = new ResponseHandler<>(
                    result, // true
                    HttpStatus.OK.value(), // 200
                    "모집완료 (COMPLETED)"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("completedMeeting 호출 중 예외 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // meetingStatus 상태 확인
    @PutMapping("/{meetingId}/meetingStatus")
    public ResponseEntity<ResponseHandler<MeetingStatusDTO>> meetingStatus(
            @PathVariable("meetingId") Long meetingId) {
        try {
            MeetingStatusDTO meetingStatusDTO = meetingService.meetingStatus(meetingId);
            log.info("meetingStatus 상태: {}", meetingStatusDTO.getMeetingStatus());

            ResponseHandler<MeetingStatusDTO> response = new ResponseHandler<>(
                    meetingStatusDTO, // true
                    HttpStatus.OK.value(), // 200
                    "meetingStatus 상태 확인"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("meetingStatus 호출 중 예외 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}