package baby.lignin.board.support.converter;

import baby.lignin.board.entity.BoardEntity;
import baby.lignin.board.model.request.BoardAddRequest;
import baby.lignin.board.model.response.BoardResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BoardConverter {
    public static BoardResponse from(BoardEntity boardEntity) {
        if (boardEntity == null) {
            return null;
        }

        return BoardResponse.builder()
                .boardId(boardEntity.getId())
                .boardName(boardEntity.getBoardName())
                .boardImage(boardEntity.getBoardImage())
                .createAt(boardEntity.getCreateAt())
                .updateAt(boardEntity.getUpdateAt())
                .build();
    }

    public BoardEntity to(BoardAddRequest request) {
        return BoardEntity.builder()
                .workspaceId(request.getWorkspaceId())
                .boardName(request.getBoardName())
                .boardImage(request.getBoardImage())
                .deleted(false)
                .build();
    }
}