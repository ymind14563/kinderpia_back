package sesac_3rd.sesac_3rd.mapper.chat;

import org.springframework.stereotype.Component;
import sesac_3rd.sesac_3rd.constant.MessageType;
import sesac_3rd.sesac_3rd.dto.chat.ChatMessageDTO;
import sesac_3rd.sesac_3rd.dto.chat.ChatRoomDTO;
import sesac_3rd.sesac_3rd.entity.ChatMessage;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMessageMapper {

    // 가장 최근 메시지 내용 및 시간
    public static ChatRoomDTO.LastMessageInfo getLastMessageInfo(ChatRoom chatRoom) {
        return chatRoom.getChatMessages().stream()
                .max(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(chatMessage -> new ChatRoomDTO.LastMessageInfo(
                        chatMessage.getChatmsgContent(),
                        chatMessage.getCreatedAt()))
                .orElse(new ChatRoomDTO.LastMessageInfo("아직 작성한 메세지가 없어요.", null)); // 기본값 반환
    }



    // 채팅방 메세지 단일 조회 (리스트용)
    public static ChatMessageDTO.ChatMessage ChatMessageToChatMessageResponseDTO(ChatMessage chatMessage) {
        // 문제: chatMessage.setSender(null); 산입으로 인해
        // sender 필드가 null 인 경우, chatMessage.getSender() 호출 시 NullPointerException이 발생할 수 있음
        // 해결방법: sender가 null인 경우 각 필드에 대해 적절한 기본값(null 또는 "System")을 설정하여
        // NullPointerException 발생을 방지

        // 기본값으로 null을 명시적으로 주는 것 (의도) 과 null인 상태 그대로 두는 것 (누락으로 판단) 은 차이가 있음
        // null 을 명시적으로 설정하면 의도적 비움으로 간주하여 예상 동작 유도 가능
        // 그대로 두면 NullPointerException 발생 위험 있으므로 기본값 설정으로 예외 방지 필요

        Long senderId = chatMessage.getSender() != null ? chatMessage.getSender().getUserId() : null;
        String senderNickname = chatMessage.getSender() != null ? chatMessage.getSender().getNickname() : "System";
        String senderProfileImg = chatMessage.getSender() != null ? chatMessage.getSender().getProfileImg() : null;


        return ChatMessageDTO.ChatMessage.builder()
                .chatmsgId(chatMessage.getChatmsgId())
                .chatroomId(chatMessage.getChatRoom().getChatroomId())
                .senderId(senderId)
                .senderNickname(senderNickname)
                .senderProfileImg(senderProfileImg)
                .chatmsgContent(chatMessage.getChatmsgContent())
                .messageType(chatMessage.getMessageType())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }



    // 채팅방 메세지 목록 조회
//    public static ChatMessageDTO.ChatMessageList ChatMessageListToChatMessageListResponseDTO(Long chatroomId, List<ChatMessage> chatMessages, int page, int pageSize, int totalPages) {
    public static ChatMessageDTO.ChatMessageList ChatMessageListToChatMessageListResponseDTO(Long chatroomId, List<ChatMessage> chatMessages) {
        List<ChatMessageDTO.ChatMessage> chatmsgList = chatMessages.stream()
                .map(ChatMessageMapper::ChatMessageToChatMessageResponseDTO)
                .collect(Collectors.toList());

        return ChatMessageDTO.ChatMessageList.builder()
                .chatroomId(chatroomId)
                .chatmsgList(chatmsgList)
//                .page(page)
//                .pageSize(pageSize)
//                .totalPages(totalPages)
                .build();
    }


    // 일반 메시지 생성 (CHAT)
    public ChatMessage ChatMessageDTOtoEntity(ChatMessageDTO.ChatMessage messageDTO, ChatRoom chatRoom, User sender) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);
        chatMessage.setChatmsgContent(messageDTO.getChatmsgContent());
        chatMessage.setMessageType(messageDTO.getMessageType());
        chatMessage.setCreatedAt(LocalDateTime.now());

        return chatMessage;
    }

    // 시스템 메시지 생성 (JOIN, LEAVE)
    public ChatMessage systemMessageToEntity(ChatRoom chatRoom, Long systemUserId, String messageContent, MessageType messageType) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setChatmsgContent(messageContent);
        chatMessage.setMessageType(messageType);
        chatMessage.setCreatedAt(LocalDateTime.now());
        chatMessage.setSender(null); // 시스템 메시지는 sender 가 null

        return chatMessage;
    }

}
