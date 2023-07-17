package baby.lignin.board.entity;
// 테이블 이름하고 일치하게 하면 좋음

import baby.lignin.board.model.request.BoardEditRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 테이블의 파라미터 형식에 맞게 맞춰준 것임.
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "boardEntity")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private Long workspaceId;

    @Column(nullable = false)
    private String boardName;

    @Column(nullable = false)
    private String boardImage;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @CreationTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    private boolean deleted;

    public void changeBoardInfo(BoardEditRequest request){
        this.boardName = request.getBoardName();
        this.boardImage = request.getBoardImage();
        this.updateAt = LocalDateTime.now();
    }

    public void delete() {
        this.deleted = true;
    }
}