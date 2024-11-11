package sesac_3rd.sesac_3rd.controller;


import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.config.security.TokenProvider;
import sesac_3rd.sesac_3rd.dto.chat.ChatMessageDTO;
import sesac_3rd.sesac_3rd.dto.chat.ChatNotificationDTO;
import sesac_3rd.sesac_3rd.dto.chat.ChatRoomDTO;
import sesac_3rd.sesac_3rd.dto.chat.ChatRoomRequestDTO;
import sesac_3rd.sesac_3rd.entity.ChatNotification;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.mapper.chat.ChatMessageMapper;
import sesac_3rd.sesac_3rd.mapper.chat.ChatRoomMapper;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomRepository;
import sesac_3rd.sesac_3rd.service.chat.ChatMessageService;
import sesac_3rd.sesac_3rd.service.chat.ChatNotificationService;
import sesac_3rd.sesac_3rd.service.chat.ChatRoomService;


@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final ChatRoomMapper chatRoomMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final TokenProvider tokenProvider;
//    private final SimpMessagingTemplate messagingTemplate;
    private final ChatNotificationService chatNotificationService;


    // 전체 채팅 목록 조회 (페이지네이션 적용)
    // /api/chatroom/list?page={page}&size={size}
    @PostMapping("/list")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<ChatRoomDTO.ChatRoomList>>> getChatRoomList(
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

//        System.out.println("UserId" + userId);

        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        PaginationResponseDTO<ChatRoomDTO.ChatRoomList> chatRooms = chatRoomService.getChatRooms(userId, page, size);

        return ResponseHandler.response(chatRooms, HttpStatus.OK, "채팅방 목록 조회 성공");
    }



    // 단일 채팅방 조회
    @PostMapping
    public ResponseEntity<ResponseHandler<ChatRoomDTO.ChatRoom>> getChatRoomById(@AuthenticationPrincipal Long userId,
                                                                                 @RequestBody ChatRoomRequestDTO chatRoomRequestDTO) {

        // 토큰에 문제가 있는 경우
        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }
        ChatRoomDTO.ChatRoom chatRoom = chatRoomService.getChatRoomById(userId, chatRoomRequestDTO.getChatroomId());

        return ResponseHandler.response(chatRoom, HttpStatus.OK, "채팅방 조회 성공");
    }


    // 메시지 저장 및 전송
    @MessageMapping("/{chatroomId}/chatmsg")
    @SendTo("/topic/chatroom/{chatroomId}")
    public ResponseEntity<ResponseHandler<ChatMessageDTO.ChatMessage>> sendMessage(@DestinationVariable("chatroomId") Long chatroomId,
                                                                                   @Header("Authorization") String token,
                                                                                   ChatMessageDTO.ChatMessage chatMessageDTO) {


        // @MessageMapping에서는 @AuthenticationPrincipal을 직접 사용할 수 없음 (WebSocket 통신, HTTP 요청 차이)
        String userId = tokenProvider.validateAndGetUserId(token.replace("Bearer ", ""));

        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        // token 의 userId를 DTO의 senderId로 직접 설정 - 값이 다를 가능성 애초에 차단
        // 그러므로 서비스에서 userId와 senderId 비교할 필요가 없어짐

        // Long.parseLong(userId) 으로 형변환하여 산입
        chatMessageDTO.setSenderId(Long.parseLong(userId));

        // 메세지 저장
        ChatMessageDTO.ChatMessage savedMessage = chatMessageService.saveMessage(chatroomId, chatMessageDTO);

        // 알림 전송
        chatNotificationService.unreadNotification(chatroomId, Long.parseLong(userId));

        return ResponseHandler.response(savedMessage, HttpStatus.OK, "채팅 메세지 전송 성공");

    }


    // 메세지 내역 조회 (페이지네이션 적용 - 채팅생성시간 오름차순 적용)
    @PostMapping("/chatmsg")
    // /api/chatroom/chatmsg/{chatroomId}?page={page}&size={size}
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<ChatMessageDTO.ChatMessageList>>> getChatMessages(
            @AuthenticationPrincipal Long userId,
            @RequestBody ChatRoomRequestDTO chatRoomRequestDTO,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "99") int size) {

        if (userId == null) {
            return ResponseHandler.unauthorizedResponse();
        }

        System.out.println(userId);
        System.out.println(chatRoomRequestDTO.getChatroomId());
        PaginationResponseDTO<ChatMessageDTO.ChatMessageList> chatMessages = chatMessageService.getChatMessages(userId, chatRoomRequestDTO.getChatroomId(), page, size);

        return ResponseHandler.response(chatMessages, HttpStatus.OK, "채팅 메시지 리스트 조회 성공");
    }

}
