package baby.lignin.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    TEST_ERROR(400, "테스트 에러 메시지입니다."),
    INCORRECT_FILE_FORMAT(400, "파일 형식이 적절하지 않습니다."),
    ;

    private int status;
    private String message;
}
