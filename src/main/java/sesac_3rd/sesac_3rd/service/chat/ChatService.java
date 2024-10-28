package sesac_3rd.sesac_3rd.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sesac_3rd.sesac_3rd.dto.chat.ChatRoomDTO;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.handler.pagination.PaginationResponseDTO;
import sesac_3rd.sesac_3rd.mapper.chat.ChatRoomMapper;
import sesac_3rd.sesac_3rd.repository.chat.ChatRoomRepository;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;


    public PaginationResponseDTO<ChatRoomDTO.ChatRoomList> getChatRooms(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<ChatRoom> chatPage = chatRoomRepository.findByUserId(userId, pageRequest);

        ChatRoomDTO.ChatRoomList chatRoomList = chatRoomMapper.ChatRoomListToChatRoomListResponseDTO(
                userId,
                chatPage.getContent(),
                chatPage.getNumber() + 1,
                chatPage.getSize(),
                chatPage.getTotalPages()
        );

        return new PaginationResponseDTO<>(chatRoomList, chatPage);

    }
}
