package sesac_3rd.sesac_3rd.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.constant.MessageType;

import java.time.LocalDateTime;
import java.util.List;

public class ChatMessageDTO {

    // 채팅 메시지 목록
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageList {
        private Long chatroomId;
        private List<ChatMessageDTO.ChatMessage> chatmsgList;
//        private int page;
//        private int pageSize;
//        private int totalPages;
    }

    // 채팅 메세지 단일 (목록, 응답용)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessage {
        private Long chatmsgId;
        private Long chatroomId;
        private Long senderId;
        private String senderNickname;
        private String senderProfileImg;
        private String chatmsgContent;
        private LocalDateTime createdAt;
        private MessageType messageType;
    }


}