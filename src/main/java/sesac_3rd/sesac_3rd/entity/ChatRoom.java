package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ChatRoom")
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id", nullable = false) // Primary Key
    private Long chatroomId;

    @OneToOne
    @JoinColumn(name = "meeting_id", referencedColumnName = "meeting_id", insertable = true, nullable = false)
    private Meeting meeting;  // Meeting과의 1:1 관계

    @Column(name = "is_active", nullable = false) // 활성화 여부
    private Boolean isActive = true; // 기본값 설정: TRUE

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL) // ChatMessage와의 1:N 관계
    private List<ChatMessage> chatMessages;

}
