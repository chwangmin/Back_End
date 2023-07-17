package baby.lignin.board.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardEditRequest {
    private Long boardId;
    private Long workspaceId;
    private String boardName;
    private String boardImage;
}