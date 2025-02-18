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


//    메시지 생성 시간을 타임스탬프(밀리초)로 변환하여 반환
//    Redis 정렬을 위해 사용 (메시지가 생성된 시간 (createdAt)을 기준으로)
//    `createdAt`이 `null`인 경우 0L을 반환하여 예외 방지
//    기존에는 메시지가 Redis 에 저장된 시간이 기준이라 정확한 순서 보장이 어려움

        public long getCreatedAtTimestamp() {
            return createdAt != null ? createdAt.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() : 0L;
        }
    }

}