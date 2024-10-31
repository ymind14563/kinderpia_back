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
    private final ChatRoomService chatService;
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
        PaginationResponseDTO<ChatRoomDTO.ChatRoomList> chatRooms  = chatService.getChatRooms(userId, page, size);

        return ResponseHandler.response(chatRooms, HttpStatus.OK, "채팅방 목록 조회 성공");
    }

    // 메시지 저장 및 전송
    @MessageMapping("/{chatroomId}/chatmsg")
    @SendTo("/topic/chatroom/{chatroomId}")
    public ResponseEntity<ResponseHandler<ChatMessageDTO.ChatMessage>> sendMessage(@DestinationVariable Long chatroomId, ChatMessageDTO.ChatMessage chatMessageDTO) {

        ChatMessageDTO.ChatMessage savedMessage = chatMessageService.saveMessage(chatroomId, chatMessageDTO);

        return ResponseHandler.response(savedMessage, HttpStatus.OK, "채팅 메세지 전송 성공");

    }

}
