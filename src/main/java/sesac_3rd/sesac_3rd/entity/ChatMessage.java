package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;
import sesac_3rd.sesac_3rd.constant.MeetingStatus;
import sesac_3rd.sesac_3rd.constant.MessageType;

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

    @ManyToOne(optional = true) // User와의 관계 설정
    @JoinColumn(name = "sender_id", nullable = true) // Foreign Key
    // 시스템 메세지를 위해 @ManyToOne 에 optional = true 추가
    // @JoinColumn 에 nullable = true 는 상위 optional 과 중복이지만 DB 용으로 명시화 하기 위해 남겨둠
    private User sender;

    @Column(name = "chatmsg_content", length = 500, nullable = false) // 채팅 메시지 내용
    private String chatmsgContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "msg_type", nullable = false, length = 10)
    private MessageType messageType = MessageType.CHAT; // 메세지 타입 (기본값: CHAT)

    @Column(name = "createdAt", nullable = false) // 생성일자
    private LocalDateTime createdAt;
}
