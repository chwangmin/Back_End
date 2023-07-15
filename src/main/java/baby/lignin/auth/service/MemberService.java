package baby.lignin.auth.service;

import baby.lignin.auth.model.Token;
import baby.lignin.auth.model.auth.KakaoTokenResponse;
import baby.lignin.auth.model.auth.KakaoUserInfoResponse;
import baby.lignin.auth.model.request.AuthRequest;

import java.util.*;


public interface MemberService {

    Token getToken(String access_code);

    public HashMap<String, Object> getUserInfo(String access_Token);

    public void unlink(String access_Token);

    public void logout(String access_Token);

    public KakaoTokenResponse getKakaoTokenResponse(String code);

    public KakaoUserInfoResponse getKakaoUserInfoResponse(String access_token);

}
