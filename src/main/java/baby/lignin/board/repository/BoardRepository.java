package baby.lignin.board.repository;

import baby.lignin.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 오른쪽에는 id 의 속성을 넣어줌
@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findByWorkspaceIdAndDeletedFalse(Long workspaceId);
    List<BoardEntity> findByWorkspaceIdAndBoardNameContaining(Long workspaceId, String searchKeyword);
}