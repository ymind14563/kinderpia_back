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
        return ChatMessageDTO.ChatMessage.builder()
                .chatmsgId(chatMessage.getChatmsgId())
                .chatroomId(chatMessage.getChatRoom().getChatroomId())
                .senderId(chatMessage.getSender().getUserId())
                .senderNickname(chatMessage.getSender().getNickname())
                .senderProfileImg(chatMessage.getSender().getProfileImg())
                .chatmsgContent(chatMessage.getChatmsgContent())
                .messageType(chatMessage.getMessageType())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }



    // 채팅방 메세지 목록 조회
    public static ChatMessageDTO.ChatMessageList ChatMessageListToChatMessageListResponseDTO(Long chatroomId, List<ChatMessage> chatMessages, int page, int pageSize, int totalPages) {
        List<ChatMessageDTO.ChatMessage> chatmsgList = chatMessages.stream()
                .map(ChatMessageMapper::ChatMessageToChatMessageResponseDTO)
                .collect(Collectors.toList());

        return ChatMessageDTO.ChatMessageList.builder()
                .chatroomId(chatroomId)
                .chatmsgList(chatmsgList)
                .page(page)
                .pageSize(pageSize)
                .totalPages(totalPages)
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
