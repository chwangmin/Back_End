package baby.lignin.board.service;

import baby.lignin.board.entity.BoardEntity;
import baby.lignin.board.model.request.BoardAddRequest;
import baby.lignin.board.model.request.BoardBrowseRequest;
import baby.lignin.board.model.request.BoardDeleteRequest;
import baby.lignin.board.model.request.BoardEditRequest;
import baby.lignin.board.model.response.BoardResponse;
import baby.lignin.board.repository.BoardRepository;
import baby.lignin.board.support.converter.BoardConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Service는 컨트롤러에서 이용함.
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {
    // new 안써도 됨. 알아서 생성, 주입을 해줌.
    private final BoardRepository boardRepository;

    public String makeRedisKey(BoardBrowseRequest request) {
        return request.getWorkspaceId() + ":" + request.getSearchKeyword();
    }

    @Cacheable(cacheNames = "boards", key = "{#root.target.makeRedisKey(#request)}")
    public List<BoardResponse> getBoards(BoardBrowseRequest request){

        List<BoardEntity> boardEntities;
        if (request.getSearchKeyword() == null) {
            boardEntities = boardRepository.findByWorkspaceIdAndDeletedFalse(request.getWorkspaceId())
                    .stream()
                    .collect(Collectors.toList());
        } else {
            boardEntities = boardRepository.findByWorkspaceIdAndBoardNameContaining(request.getWorkspaceId(), request.getSearchKeyword())
                    .stream()
                    .collect(Collectors.toList());
        }

        List<BoardResponse> responses = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntities){
            responses.add(BoardConverter.from(boardEntity));
        }
        return responses;
    }

    public BoardResponse generateBoard(BoardAddRequest request) {
        BoardEntity boardEntity = boardRepository.save(BoardConverter.to(request));

        return BoardConverter.from(boardEntity);
    }

    public BoardResponse changeBoardInfo(BoardEditRequest request) {
        BoardEntity boardEntity = boardRepository.findById(request.getBoardId()).orElseThrow();
        boardEntity.changeBoardInfo(request);
        return BoardConverter.from(boardEntity);
    }

    public void deleteBoard(BoardDeleteRequest request) {
        boardRepository.deleteById(request.getBoardId());
    }
}