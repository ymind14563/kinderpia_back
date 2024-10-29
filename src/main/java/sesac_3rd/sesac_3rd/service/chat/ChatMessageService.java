package sesac_3rd.sesac_3rd.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.chat.ChatMessageDTO;
import sesac_3rd.sesac_3rd.entity.ChatMessage;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.User;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.mapper.chat.ChatMessageMapper;
import sesac_3rd.sesac_3rd.repository.UserMeetingRepository;
import sesac_3rd.sesac_3rd.repository.UserRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatMessageRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final UserMeetingRepository userMeetingRepository;

    // 메세지 저장
    public ChatMessageDTO.ChatMessage saveMessage(Long chatroomId, ChatMessageDTO.ChatMessage chatMessageDTO) {

        // ChatRoom 과 User 존재 여부 확인
        ChatRoom chatRoom = chatRoomRepository.findById(chatroomId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.CHATROOM_NOT_FOUND));
        User sender = userRepository.findById(chatMessageDTO.getSenderId())
                .orElseThrow(() -> new CustomException(ExceptionStatus.USER_NOT_FOUND));

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

        ChatMessage chatMessage = chatMessageMapper.ChatMessageDTOtoEntity(chatMessageDTO, chatRoom, sender);
        chatMessageRepository.save(chatMessage);
        return chatMessageMapper.ChatMessageToChatMessageResponseDTO(chatMessage);
    }



}

