package baby.lignin.board.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardBrowseRequest {
    private Long workspaceId;
    private String searchKeyword;
}