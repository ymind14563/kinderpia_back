package sesac_3rd.sesac_3rd.controller;


import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.dto.chat.ChatMessageDTO;
import sesac_3rd.sesac_3rd.dto.chat.ChatRoomDTO;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.mapper.chat.ChatMessageMapper;
import sesac_3rd.sesac_3rd.mapper.chat.ChatRoomMapper;
import sesac_3rd.sesac_3rd.service.chat.ChatMessageService;
import sesac_3rd.sesac_3rd.service.chat.ChatRoomService;


@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final ChatRoomMapper chatRoomMapper;
    private final ChatMessageMapper chatMessageMapper;
//    private final SimpMessagingTemplate messagingTemplate;

    // 전체 채팅 목록 조회 (페이지네이션 적용)
    // /api/chatroom/list/{userId}?page={page}&size={size}
    @GetMapping("/list/{userId}")
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<ChatRoomDTO.ChatRoomList>>> getChatRoomList(@Positive @PathVariable("userId") Long userId,
                                                                                                            @RequestParam int page,
                                                                                                            @RequestParam int size)
    {
        PaginationResponseDTO<ChatRoomDTO.ChatRoomList> chatRooms  = chatRoomService.getChatRooms(userId, page, size);

        return ResponseHandler.response(chatRooms, HttpStatus.OK, "채팅방 목록 조회 성공");
    }


    // 단일 채팅방 조회
    @GetMapping("/{chatroomId}")
    public ResponseEntity<ResponseHandler<ChatRoomDTO.ChatRoom>> getChatRoomById(@Positive @PathVariable Long chatroomId) {
        ChatRoomDTO.ChatRoom chatRoom = chatRoomService.getChatRoomById(chatroomId);
        return ResponseHandler.response(chatRoom, HttpStatus.OK, "채팅방 조회 성공");
    }



    // 메시지 저장 및 전송
    @MessageMapping("/{chatroomId}/chatmsg")
    @SendTo("/topic/chatroom/{chatroomId}")
    public ResponseEntity<ResponseHandler<ChatMessageDTO.ChatMessage>> sendMessage(@DestinationVariable Long chatroomId, ChatMessageDTO.ChatMessage chatMessageDTO) {

        ChatMessageDTO.ChatMessage savedMessage = chatMessageService.saveMessage(chatroomId, chatMessageDTO);

        return ResponseHandler.response(savedMessage, HttpStatus.OK, "채팅 메세지 전송 성공");

    }



    // 메세지 내역 조회 (페이지네이션 적용 - 채팅생성시간 오름차순 적용)
    @GetMapping("/chatmsg/{chatroomId}")
    // /api/chatroom/chatmsg/{chatroomId}?page={page}&size={size}
    public ResponseEntity<ResponseHandler<PaginationResponseDTO<ChatMessageDTO.ChatMessageList>>> getChatMessages(@PathVariable("chatroomId") Long chatroomId,
                                                                                                                  @RequestParam int page,
                                                                                                                  @RequestParam int size) {

        PaginationResponseDTO<ChatMessageDTO.ChatMessageList> chatMessages = chatMessageService.getChatMessages(chatroomId, page, size);

        return ResponseHandler.response(chatMessages, HttpStatus.OK, "채팅 메시지 리스트 조회 성공");
    }



}
