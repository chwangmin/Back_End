package baby.lignin.board.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardAddRequest {
    private Long workspaceId;
    private String boardName;
    private String boardImage;

    @Override
    public String toString() {
        return "BoardAddRequest{" +
                "workspaceId=" + workspaceId +
                ", boardName=" + boardName +
                ", boardImage=" + boardImage +
                '}';
    }
}