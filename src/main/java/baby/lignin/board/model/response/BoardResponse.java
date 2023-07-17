package baby.lignin.board.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor // 이거 추가해야 NoArgsConstructor Builder 랑 같이 사용 가능합니다!
public class BoardResponse {
    private Long boardId;
    private String boardName;
    private String boardImage;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}