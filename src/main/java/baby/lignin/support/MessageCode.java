package baby.lignin.support;

import lombok.Getter;

@Getter
public enum MessageCode {
    SUCCESS("success", "성공");

    private final String code;
    private final String value;

    MessageCode(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
