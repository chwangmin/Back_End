package baby.lignin.auth.model.auth;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoProfile {
    private String nickname;
    private String thumbnailImageUrl;
    private String profileImageUrl;
}
