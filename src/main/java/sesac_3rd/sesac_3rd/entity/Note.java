package sesac_3rd.sesac_3rd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Note")
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id", nullable = false) // Primary Key
    private Long noteId;

    @ManyToOne // 발신자와의 관계 설정
    @JoinColumn(name = "sender_id", nullable = false) // Foreign Key
    private User sender;    // 발신자

    @ManyToOne // 수신자와의 관계 설정
    @JoinColumn(name = "receiver_id", nullable = false) // Foreign Key
    private User receiver;    // 수신자

    @Column(name = "note_content", length = 500, nullable = false) // 쪽지 내용
    private String noteContent;   // 쪽지내용

    @Column(name = "createdAt", nullable = false) // 생성일자
    private LocalDateTime createdAt;    // 생성일자
}
