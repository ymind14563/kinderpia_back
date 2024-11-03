package sesac_3rd.sesac_3rd.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.chat.ChatRoomDTO;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.entity.UserMeeting;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.mapper.chat.ChatRoomMapper;
import sesac_3rd.sesac_3rd.repository.MeetingRepository;
import sesac_3rd.sesac_3rd.repository.UserMeetingRepository;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final MeetingRepository meetingRepository;
    private final UserMeetingRepository userMeetingRepository;




    // 유저 소속 채팅방 목록 조회
    public PaginationResponseDTO<ChatRoomDTO.ChatRoomList> getChatRooms(Long userId, int page, int size) {


        PageRequest pageRequest = PageRequest.of(page - 1, size);

        // userMeeting 에서 승인된 모임 조회
        List<Long> meetingWithAccept = userMeetingRepository.findByUser_UserIdAndIsAcceptedTrue(userId)
                .stream()
                .map(userMeeting -> userMeeting.getMeeting().getMeetingId())
                .collect(Collectors.toList());

        // meetingId 리스트를 기반으로 채팅방 목록 조회
        Page<ChatRoom> chatPage = chatRoomRepository.findByMeeting_MeetingIdIn(meetingWithAccept, pageRequest);


        ChatRoomDTO.ChatRoomList chatRoomList = chatRoomMapper.ChatRoomListToChatRoomListResponseDTO(
                userId,
                chatPage.getContent()
//                chatPage.getNumber() + 1,
//                chatPage.getSize(),
//                chatPage.getTotalPages()
        );

        return new PaginationResponseDTO<>(chatRoomList, chatPage);

    }

    // 채팅방 생성
    public void createChatRoomIfNotExists(Long meetingId) {

        // Meeting 의 ChatRoom 존재 여부 확인
        boolean chatRoomExists = chatRoomRepository.existsByMeeting_MeetingId(meetingId);

        if (!chatRoomExists) {
            Meeting meeting = meetingRepository.findById(meetingId)
                    .orElseThrow(() -> new CustomException(ExceptionStatus.MEETING_NOT_FOUND));

            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setMeeting(meeting);
            chatRoom.setIsActive(true);

            chatRoomRepository.save(chatRoom);
        }
    }


    // 단일 채팅방 조회
    public ChatRoomDTO.ChatRoom getChatRoomById(Long userId, Long chatroomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatroomId)
                .orElseThrow(() -> new CustomException(ExceptionStatus.CHATROOM_NOT_FOUND));

        // 유저가 해당 채팅방에 소속되어 있는지 확인
        boolean isUserInMeeting = userMeetingRepository.existsByUser_UserIdAndMeeting_MeetingIdAndIsAcceptedTrue(userId, chatRoom.getMeeting().getMeetingId());
        if (!isUserInMeeting) {
            throw new CustomException(ExceptionStatus.USER_NOT_IN_CHATROOM);
        }

        List<UserMeeting> userMeetingListByMeetingId = userMeetingRepository.findByMeeting_MeetingId(chatRoom.getMeeting().getMeetingId());

        // 유저미팅 테이블에서 같은 meetingId, accept 상태인 유저 리스트 필터링
        List<UserMeeting> userListInMeeting = userMeetingListByMeetingId.stream()
                .filter(userMeeting -> Boolean.TRUE.equals(userMeeting.getIsAccepted()))
                .collect(Collectors.toList());

        return ChatRoomMapper.ChatRoomToChatRoomResponseDTO(chatRoom, userListInMeeting);
    }
}
