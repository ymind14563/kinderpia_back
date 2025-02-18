package sesac_3rd.sesac_3rd.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.constant.MessageType;
import sesac_3rd.sesac_3rd.dto.chat.ChatMessageDTO;
import sesac_3rd.sesac_3rd.entity.ChatMessage;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.mapper.chat.ChatMessageMapper;
import sesac_3rd.sesac_3rd.repository.UserMeetingRepository;
import sesac_3rd.sesac_3rd.repository.UserRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatMessageRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomRepository;
import sesac_3rd.sesac_3rd.repository.chat.RedisChatMessageRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final UserMeetingRepository userMeetingRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisChatMessageRepository redisChatMessageRepository;

    // 메세지 저장
    public ChatMessageDTO.ChatMessage saveMessage(Long chatroomId, ChatMessageDTO.ChatMessage chatMessageDTO) {

        log.info("[WebSocket] 메시지 도착: chatroomId={}, senderId={}, content={}",
        chatroomId, chatMessageDTO.getSenderId(), chatMessageDTO.getChatmsgContent());

        // ChatRoom 과 User 존재 여부 확인
        ChatRoom chatRoom = chatRoomRepository.findById(chatroomId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.CHATROOM_NOT_FOUND));

        // senderId 로 User 존재 여부 확인
        User sender = userRepository.findById(chatMessageDTO.getSenderId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

        // 컨트롤러에서 직접 주입하면 senderId와 token의 userId를 비교할 필요가 없어짐
//        // senderId와 token 에 userId가 일치하는지 확인
//        if (!sender.getUserId().equals(userId)) {
//            throw new CustomException(ExceptionStatus.CHATMSG_USER_NOT_MATCH);
//        }

        // 채팅방 활동 여부 확인
        if (!chatRoom.getIsActive()) {
            throw new CustomException(ExceptionStatus.CHATROOM_NOT_ACTIVE);
        }

        // sender 가 채팅방에 소속된 유저인지 확인
        boolean isSenderInMeeting = userMeetingRepository.existsByUser_UserIdAndMeeting_MeetingId(
                chatMessageDTO.getSenderId(), chatRoom.getMeeting().getMeetingId());

        if (!isSenderInMeeting) {
            throw new CustomException(ExceptionStatus.USER_NOT_IN_CHATROOM);
        }

//        // 메시지 타입에 따른 처리
//        if (chatMessageDTO.getMessageType() == MessageType.CHAT) {
//            return chatTypeMessage(chatRoom, chatMessageDTO);
//        } else if (chatMessageDTO.getMessageType() == MessageType.JOIN) {
//            return sendJoinMessage(chatRoom, chatMessageDTO.getChatmsgContent());
//        } else if (chatMessageDTO.getMessageType() == MessageType.LEAVE) {
//            return sendLeaveMessage(chatRoom, chatMessageDTO.getChatmsgContent());
//        }
//        throw new CustomException(ExceptionStatus.INVALID_MESSAGE_TYPE);


//        // WebSocket 을 통해 메시지 전송 // @SendTo 제거
//        ChatMessageDTO.ChatMessage responseMessage = chatMessageMapper.ChatMessageToChatMessageResponseDTO(chatMessage);
//        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoom.getChatroomId(), responseMessage);
//
//        return responseMessage;

        ChatMessage chatMessage = chatMessageMapper.ChatMessageDTOtoEntity(chatMessageDTO, chatRoom, sender);

        // db 저장
        chatMessageRepository.save(chatMessage);
        log.info("[DB] 메시지 저장 완료");

        // redis 저장
        redisChatMessageRepository.saveMessageToRedis(chatroomId, chatMessageDTO);
        log.info("[Redis] 메시지 Redis 저장 완료: {}", chatMessageDTO);

        return chatMessageMapper.ChatMessageToChatMessageResponseDTO(chatMessage);
    }


//    // 일반 메시지 처리 (TYPE: CHAT)
//    private ChatMessageDTO.ChatMessage chatTypeMessage(ChatRoom chatRoom, ChatMessageDTO.ChatMessage chatMessageDTO) {
//
//        // sender 존재 여부 확인
//        User sender = userRepository.findById(chatMessageDTO.getSenderId())
//                .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));
//
//        // sender 가 채팅방에 소속된 유저인지 확인
//        boolean isSenderInMeeting = userMeetingRepository.existsByUser_UserIdAndMeeting_MeetingId(
//                chatMessageDTO.getSenderId(), chatRoom.getMeeting().getMeetingId());
//
//        if (!isSenderInMeeting) {
//            throw new CustomException(ExceptionStatus.USER_NOT_IN_CHATROOM);
//        }
//
//        // 메시지 저장
//        ChatMessage chatMessage = chatMessageMapper.ChatMessageDTOtoEntity(chatMessageDTO, chatRoom, sender);
//        chatMessageRepository.save(chatMessage);
//        return chatMessageMapper.ChatMessageToChatMessageResponseDTO(chatMessage);
//    }

    // 참가 메시지 (TYPE: JOIN) - @SendTo 없이 직접 전송 - meetingService 에서 활용
    public void sendJoinMessage(ChatRoom chatRoom, String userNickname) {
        String messageContent = userNickname + "님이 입장하셨습니다.";
        ChatMessage chatMessage = chatMessageMapper.systemMessageToEntity(chatRoom, null, messageContent, MessageType.JOIN);
        chatMessageRepository.save(chatMessage);

        // 메시지 전송
        ChatMessageDTO.ChatMessage responseMessage = chatMessageMapper.ChatMessageToChatMessageResponseDTO(chatMessage);
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoom.getChatroomId(), responseMessage);
        log.info("입장 시스템 채팅메세지: {}", responseMessage);
    }

    // 퇴장 메시지 (TYPE: LEAVE) - @SendTo 없이 직접 전송 - meetingService 에서 활용
    public void sendLeaveMessage(ChatRoom chatRoom, String userNickname) {
        String messageContent = userNickname + "님이 퇴장하셨습니다.";
        ChatMessage chatMessage = chatMessageMapper.systemMessageToEntity(chatRoom, null, messageContent, MessageType.LEAVE);
        chatMessageRepository.save(chatMessage);

        // 메시지 전송
        ChatMessageDTO.ChatMessage responseMessage = chatMessageMapper.ChatMessageToChatMessageResponseDTO(chatMessage);
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoom.getChatroomId(), responseMessage);
        log.info("퇴장 시스템 채팅메세지: {}", responseMessage);

    }


    // 채팅 메시지 목록 조회
    public PaginationResponseDTO<ChatMessageDTO.ChatMessageList> getChatMessages(Long userId, Long chatroomId, int page, int size) {

        // ChatRoom 존재 여부 확인
        ChatRoom chatRoom = chatRoomRepository.findById(chatroomId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.CHATROOM_NOT_FOUND));

        // 유저가 해당 채팅방에 소속되어 있는지 확인
        boolean isUserInMeeting = userMeetingRepository.existsByUser_UserIdAndMeeting_MeetingIdAndIsAcceptedTrue(userId, chatRoom.getMeeting().getMeetingId());

        if (!isUserInMeeting) {
            throw new CustomException(ExceptionStatus.USER_NOT_IN_CHATROOM);
        }

        // 생성시간으로 내림차순
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));


        // Redis 에서 데이터 조회 (페이지네이션 적용)
        List<ChatMessageDTO.ChatMessage> redisMessages = redisChatMessageRepository.getMessagesFromRedis(chatroomId, page, size);

        // Redis 데이터가 충분하면 그대로 반환 (DB 조회 안함)
        if (!redisMessages.isEmpty() && redisMessages.size() >= size) {
            return new PaginationResponseDTO<>(
                    chatMessageMapper.ChatMessageListDTOToResponseDTO(chatroomId, redisMessages),null
            );
        }

        // 부족한 메시지 수 계산
        int remainingSize = size - redisMessages.size();

        // Redis 에서 가장 오래된 메시지의 createdAt 확인
        LocalDateTime lastRedisCreatedAt = redisMessages.isEmpty() ? null : redisMessages.get(redisMessages.size() - 1).getCreatedAt();

        //  ️DB 에서 부족한 메시지 수만큼 추가 조회 (Redis 에 없는 더 오래된 메시지 가져옴)
        Pageable pageable = PageRequest.of(0, remainingSize, sort);

        List<ChatMessage> dbMessages = chatMessageRepository.findOlderMessages(chatroomId, lastRedisCreatedAt, pageable);

        // Redis 메시지를 기준으로 리스트 생성 (Redis 데이터 우선)
        List<ChatMessageDTO.ChatMessage> mergedMessages = new ArrayList<>(redisMessages);

        // DB 에서 가져온 메시지를 DTO 로 변환 후 리스트에 추가
        // Redis + DB 데이터 병합
        List<ChatMessageDTO.ChatMessage> dbMessagesDTO =
                chatMessageMapper.ChatMessageListToChatMessageListResponseDTO(chatroomId, dbMessages).getChatmsgList();
        mergedMessages.addAll(dbMessagesDTO);


        return new PaginationResponseDTO<>(
                chatMessageMapper.ChatMessageListDTOToResponseDTO(chatroomId, mergedMessages), null
        );
    }
}
