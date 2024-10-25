package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ChatMessage")
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatmsg_id", nullable = false) // Primary Key
    private Long chatmsgId;

    @ManyToOne // ChatRoom과의 관계 설정
    @JoinColumn(name = "chatroom_id", nullable = false) // Foreign Key
    private ChatRoom chatRoom;

    @ManyToOne // User와의 관계 설정
    @JoinColumn(name = "sender_id", nullable = false) // Foreign Key
    private User sender;

    @Column(name = "chatmsg_content", length = 500, nullable = false) // 채팅 메시지 내용
    private String chatmsgContent;

    @Column(name = "createdAt", nullable = false) // 생성일자
    private LocalDateTime createdAt;
}
