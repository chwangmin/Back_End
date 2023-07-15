package baby.lignin.auth.service;

import baby.lignin.auth.entity.MemberEntity;
import baby.lignin.auth.model.Token;
import baby.lignin.auth.model.auth.KakaoTokenResponse;
import baby.lignin.auth.model.auth.KakaoUserInfoResponse;
import baby.lignin.auth.repository.MemberRepository;
import baby.lignin.auth.util.TokenGenerator;
import baby.lignin.auth.util.converter.MemberConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final TokenGenerator tokenGenerator;
    private final String kakaoAuthUri = "https://kauth.kakao.com";
    private final String kakaoUri = "https://kapi.kakao.com";

    @Value("${spring.auth.kakao.client_id}")
    String client_id;

    @Value("${spring.auth.kakao.redirect_uri}")
    String redirect_uri;
    @Override
    public Token getToken(String access_code) {
        KakaoTokenResponse kakaoTokenResponse = getKakaoTokenResponse(access_code);
        KakaoUserInfoResponse kakaoUserInfoResponse = getKakaoUserInfoResponse(kakaoTokenResponse.getAccessToken());
        System.out.println(kakaoUserInfoResponse.getKakaoAccount().getProfile().getNickname());

        //Id 중복 제거
        MemberEntity existMemberEntity = memberRepository.findByCertificationId(kakaoUserInfoResponse.getId()).orElse(null);

        if(existMemberEntity != null){
            Long memberId = existMemberEntity.getId();
            //tokenStorage.storeRefreshToken(token.getRefreshToken(), memberId);
            return tokenGenerator.generateToken(memberId);
        }

        MemberEntity memberEntity = memberRepository.save(MemberConverter.to(kakaoUserInfoResponse));
        System.out.println(memberEntity.getId());
        Long memberId = memberEntity.getId();

        return tokenGenerator.generateToken(memberId);
    }

    @Override
    public HashMap<String, Object> getUserInfo(String access_Token) {
// 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            System.out.println("result type" + result.getClass().getName()); // java.lang.String

            try {
                // jackson objectmapper 객체 생성
                ObjectMapper objectMapper = new ObjectMapper();
                // JSON String -> Map
                Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
                });

                System.out.println(jsonMap.get("properties"));

                Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
                Map<String, Object> kakao_account = (Map<String,Object>) jsonMap.get("kakao_account");

                System.out.println(properties.get("nickname"));
                System.out.println(properties.get("profile_image"));
                System.out.println(kakao_account.get("email"));

                String nickname = properties.get("nickname").toString();
                String profile_image = properties.get("profile_image").toString();
                String email = kakao_account.get("email").toString();

                userInfo.put("nickname", nickname);
                userInfo.put("profile_image", profile_image);
                userInfo.put("email", email);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    @Override
    public void unlink(String access_Token) {
        String reqURL = "https://kapi.kakao.com/v1/user/unlink";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout(String access_Token) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO : 여기 작성
    @Override
    public KakaoTokenResponse getKakaoTokenResponse(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl(kakaoAuthUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", client_id)
                        .queryParam("code", code)
                        .queryParam("redirect_uri", redirect_uri)
                        .build())
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();
    }

    @Override
    public KakaoUserInfoResponse getKakaoUserInfoResponse(String access_token) {
        WebClient webClient;
        webClient = WebClient.builder()
                .baseUrl(kakaoUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();


        KakaoUserInfoResponse kakaoUserInfoResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v2/user/me")
                        .build())
                .header("Authorization", "Bearer " + access_token)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();

        System.out.println(kakaoUserInfoResponse.getKakaoAccount().getProfile());
        return kakaoUserInfoResponse;
    }


}
