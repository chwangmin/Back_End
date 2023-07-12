package baby.lignin.auth.model.auth;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoProfile {
    public String nickname;
    public String profile_image;
    public String thumbnail_image;
}
