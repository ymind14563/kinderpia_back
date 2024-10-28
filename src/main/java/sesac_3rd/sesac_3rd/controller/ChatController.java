package sesac_3rd.sesac_3rd.controller;


import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import sesac_3rd.sesac_3rd.dto.chat.ChatRoomDTO;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.handler.ResponseHandler;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.mapper.chat.ChatMessageMapper;
import sesac_3rd.sesac_3rd.mapper.chat.ChatRoomMapper;
import sesac_3rd.sesac_3rd.service.chat.ChatService;


@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
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
}
