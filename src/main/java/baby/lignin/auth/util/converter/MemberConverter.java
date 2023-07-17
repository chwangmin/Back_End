package baby.lignin.auth.util.converter;


import baby.lignin.auth.entity.MemberEntity;
import baby.lignin.auth.model.auth.KakaoUserInfoResponse;
import baby.lignin.auth.model.response.MemberResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class MemberConverter {
    public static MemberEntity to(MemberResponse memberResponse) {
        return MemberEntity.builder()
                .name(memberResponse.getName())
                .profile(memberResponse.getProfile())
                .email(memberResponse.getEmail())
                .build();
    }

    public static MemberResponse from(MemberEntity memberEntity) {
        return MemberResponse.builder()
                .memberId(memberEntity.getId())
                .name(memberEntity.getName())
                .profile(memberEntity.getProfile())
                .email(memberEntity.getEmail())
                .build();
    }

    public static MemberEntity to(KakaoUserInfoResponse kakaoUserInfoResponse) {
        return MemberEntity.builder()
                .id(kakaoUserInfoResponse.getId())
                .profile(kakaoUserInfoResponse.getKakaoAccount()
                        .getProfile()
                        .getProfileImageUrl())
                .email(kakaoUserInfoResponse.getKakaoAccount()
                        .getEmail())
                .name(kakaoUserInfoResponse.getKakaoAccount()
                        .getProfile()
                        .getNickname())
                .certificationId(kakaoUserInfoResponse.getId())
                .build();
    }
}
