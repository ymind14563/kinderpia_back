package sesac_3rd.sesac_3rd.controller;

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
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.service.meeting.MeetingService;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {
    @Autowired
    private TokenProvider tokenProvider;

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

    // 모임 목록 (open - 열려있는 것만 정렬)
    @GetMapping("/list/open")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<MeetingDTO>>> getOpenMeetings(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "6") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        PaginationResponseDTO<MeetingDTO> paginationResponse = meetingService.getOpenMeetings(pageable);

        return ResponseEntity.ok(
                new ResponseHandler<>(paginationResponse, HttpStatus.OK.value(), "모임 목록 조회[open] 완료")
        );
    }

    // 키워드로 모임 검색
    @GetMapping("/list/search")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<MeetingDTO>>> getSearchMeetingsByKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "6") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        PaginationResponseDTO<MeetingDTO> paginationResponse = meetingService.searchMeetingsByKeyword(keyword, pageable);

        return ResponseEntity.ok(
                new ResponseHandler<>(paginationResponse, HttpStatus.OK.value(), "모임 목록 조회[keyword] 완료")
        );
    }

    // 모임 상세조회 (모임장 정보 포함)
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
            throw new RuntimeException(e);
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

            ResponseHandler<Long> response = new ResponseHandler<>(
                    meeting.getMeetingId(), // 생성된 meetingId 포함
                    HttpStatus.CREATED.value(), // 200
                    "모임 생성 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 모임 수정
    @PutMapping("/{meetingId}")
    public ResponseEntity<ResponseHandler<Void>> updateMeeting(
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

            meetingService.updateMeeting(userId, meetingId, dto);

            ResponseHandler<Void> response = new ResponseHandler<>(
                    null,
                    HttpStatus.NO_CONTENT.value(), // 204
                    "모임 수정 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 모임 삭제
    @PutMapping("/{meetingId}/delete")
    public ResponseEntity<ResponseHandler<Boolean>> deleteMeeting(
            @AuthenticationPrincipal Long userId,
            @PathVariable("meetingId") Long meetingId) {
        try {
            // 토큰에 문제가 있는 경우
            if (userId == null) {
                System.out.println("userId 토큰없음 >> " + userId);
                return ResponseHandler.unauthorizedResponse();
            }
            System.out.println("userId 토큰있음 >> " + userId);

            Boolean result = meetingService.deleteMeeting(userId, meetingId);

            ResponseHandler<Boolean> response = new ResponseHandler<>(
                    result, // true
                    HttpStatus.OK.value(), // 200
                    "모임 삭제 완료"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

