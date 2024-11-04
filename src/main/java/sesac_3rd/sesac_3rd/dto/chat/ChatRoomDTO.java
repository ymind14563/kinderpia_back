package sesac_3rd.sesac_3rd.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac_3rd.sesac_3rd.dto.user.UserDTO;
import sesac_3rd.sesac_3rd.entity.MeetingCategory;


import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomDTO {

    // 채팅방 목록
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomList {
        private Long userId;
        private List<ChatRoomDTO.ChatRoom> chatroomList;
//        private int page;
//        private int pageSize;
//        private int totalPages;
    }

    // 채팅방 단일
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoom {
        private Long chatroomId;
        private Long meetingId;
        private String meetingTitle;
        private Long meetingHeader;
        private String meetingCategoryName;
        private String lastMessage;
        private LocalDateTime lastMessageCreatedAt;
        private int capacity;
        private boolean isActive;
        private List<UserInfo> users;
    }

    // 채팅방 소속 유저 기본 정보
    // 추후 UserDTO로 옮겨야 함
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String nickname;
        private String profileImg;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LastMessageInfo {
        private String messageContent;
        private LocalDateTime createdAt;
    }


}
