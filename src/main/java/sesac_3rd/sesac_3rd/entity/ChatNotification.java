package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ChatNotification", uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "chatroom_id"}) })
@Entity
public class ChatNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatnoti_id", nullable = false) // Primary Key
    private Long chatnotiId;

    @ManyToOne(fetch = FetchType.LAZY) // User와 다대일 관계
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // ChatRoom과 다대일 관계
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(name = "unread_count", nullable = false) // 안 읽은 메시지 수
    private int unreadCount;
}
