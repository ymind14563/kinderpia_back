package sesac_3rd.sesac_3rd.mapper.chat;


import org.springframework.stereotype.Component;
import sesac_3rd.sesac_3rd.dto.chat.ChatMessageDTO;
import sesac_3rd.sesac_3rd.dto.chat.ChatRoomDTO;
import sesac_3rd.sesac_3rd.entity.*;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ChatRoomMapper {

//    // 채팅방 단일 조회
//    public static ChatRoomDTO.ChatRoom ChatRoomToChatRoomResponseDTO(ChatRoom chatRoom, Meeting meeting) {
//        return ChatRoomDTO.ChatRoom.builder()
//                .chatroomId(chatRoom.getChatroomId())
//                .meetingId(meeting.getMeetingId())
//                .meetingTitle(meeting.getMeetingTitle())
//                .meetingCategory(meeting.getMeetingCategory())
//                .lastMessage(getLastMessageContent(chatRoom))
//                .totalCapacity(meeting.getTotalCapacity())
//                .isActive(chatRoom.getIsActive())
//                .build();
//    }




    // 채팅방 단일 조회 (소속 유저 포함)
    public static ChatRoomDTO.ChatRoom ChatRoomToChatRoomResponseDTO(ChatRoom chatRoom, List<UserMeeting> userMeetings) {
        List<ChatRoomDTO.UserInfo> userListInChatRoom = userMeetings.stream()
                .map(userMeeting -> {
                    User user = userMeeting.getUser();
                    return ChatRoomDTO.UserInfo.builder()
                            .userId(user.getUserId())
                            .nickname(user.getNickname())
                            .profileImg(user.getProfileImg())
                            .build();
                })
                .collect(Collectors.toList());

        ChatRoomDTO.LastMessageInfo lastMessageInfo = ChatMessageMapper.getLastMessageInfo(chatRoom);


        return ChatRoomDTO.ChatRoom.builder()
                .chatroomId(chatRoom.getChatroomId())
                .meetingId(chatRoom.getMeeting().getMeetingId())
                .meetingTitle(chatRoom.getMeeting().getMeetingTitle())
                .meetingHeader(chatRoom.getMeeting().getUser().getUserId())
                .meetingCategoryName(chatRoom.getMeeting().getMeetingCategory().getMeetingCtgName())
                .lastMessage(lastMessageInfo.getMessageContent())
                .lastMessageCreatedAt(lastMessageInfo.getCreatedAt())
                .capacity(chatRoom.getMeeting().getCapacity())
                .isActive(chatRoom.getIsActive())
                .users(userListInChatRoom)
                .build();

    }





    // 채팅방 목록 조회
    public static ChatRoomDTO.ChatRoomList ChatRoomListToChatRoomListResponseDTO(Long userId, List<ChatRoom> chatRooms) {



        List<ChatRoomDTO.ChatRoom> chatroomList = chatRooms.stream()
                .map(chatRoom -> {
                    ChatRoomDTO.LastMessageInfo lastMessageInfo = ChatMessageMapper.getLastMessageInfo(chatRoom);

                    return ChatRoomDTO.ChatRoom.builder()
                            .chatroomId(chatRoom.getChatroomId())
                            .meetingId(chatRoom.getMeeting().getMeetingId())
                            .meetingTitle(chatRoom.getMeeting().getMeetingTitle())
                            .meetingHeader(chatRoom.getMeeting().getUser().getUserId())
                            .meetingCategoryName(chatRoom.getMeeting().getMeetingCategory().getMeetingCtgName())
                            .lastMessage(lastMessageInfo.getMessageContent())
                            .lastMessageCreatedAt(lastMessageInfo.getCreatedAt())
                            .capacity(chatRoom.getMeeting().getCapacity())
                            .isActive(chatRoom.getIsActive())
                            .build();
                })
                .collect(Collectors.toList());


        return ChatRoomDTO.ChatRoomList.builder()
                .userId(userId)
                .chatroomList(chatroomList)
//                .page(page)
//                .pageSize(pageSize)
//                .totalPages(totalPages)
                .build();
    }

}
