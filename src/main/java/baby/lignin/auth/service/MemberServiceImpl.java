package baby.lignin.auth.service;

import baby.lignin.auth.config.TokenResolver;
import baby.lignin.auth.entity.MemberEntity;
import baby.lignin.auth.model.Token;
import baby.lignin.auth.model.auth.KakaoTokenResponse;
import baby.lignin.auth.model.auth.KakaoUserInfoResponse;
import baby.lignin.auth.model.response.MemberResponse;
import baby.lignin.auth.repository.MemberRepository;
import baby.lignin.auth.util.TokenGenerator;
import baby.lignin.auth.util.converter.MemberConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final TokenGenerator tokenGenerator;
    private final TokenResolver tokenResolver;
    private final String kakaoAuthUri = "https://kauth.kakao.com";
    private final String kakaoUri = "https://kapi.kakao.com";

    @Value("${spring.auth.kakao.client_id}")
    String client_id;

    @Value("${spring.auth.kakao.redirect_uri}")
    String redirect_uri;

    @Value("${spring.auth.kakao.admin_key}")
    String admin_key;

    @Override
    public Token getToken(String access_code) {
        KakaoTokenResponse kakaoTokenResponse = getKakaoTokenResponse(access_code);
        KakaoUserInfoResponse kakaoUserInfoResponse = getKakaoUserInfoResponse(kakaoTokenResponse.getAccessToken());
        //Id 중복 제거
        MemberEntity existMemberEntity = memberRepository.findByCertificationIdAndDeletedFalse(kakaoUserInfoResponse.getId()).orElse(null);
        if (existMemberEntity != null) {
            Long memberId = existMemberEntity.getId();
            //tokenStorage.storeRefreshToken(token.getRefreshToken(), memberId);
            return tokenGenerator.generateToken(memberId);
        }

        MemberEntity memberEntity = memberRepository.save(MemberConverter.to(kakaoUserInfoResponse));
        Long memberId = memberEntity.getId();

        return tokenGenerator.generateToken(memberId);
    }


    @Override
    public MemberResponse getUserInfo(String token) throws Exception {
        Optional<Long> memberIdRe = tokenResolver.resolveToken(token);

        if (memberIdRe.isPresent()) {
            Long memberId = memberIdRe.get();
            MemberEntity memberEntity = memberRepository.findById(memberId)
                    .orElseThrow(() -> new Exception("찾는 멤버가 없습니다.!"));
            return MemberConverter.from(memberEntity);

        }
        return null;

    }

    @Override
    public void unlink(String access_Token) {

        Optional<Long> memberIdRe = tokenResolver.resolveToken(access_Token);
        Long memberId = memberIdRe.get();
        MemberEntity memberEntity = memberRepository.findById(memberId).orElse(null);

        WebClient webClient = WebClient.builder()
                .baseUrl(kakaoUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + admin_key)
                .build();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("target_id_type", "user_id");
        requestBody.add("target_id", String.valueOf(memberEntity.getCertificationId()));
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/user/unlink")
                        .build())
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .toBodilessEntity()
                .block();

        memberRepository.delete(memberEntity);

    }

    @Override
    public void logout(String access_Token) {
        Optional<Long> memberIdRe = tokenResolver.resolveToken(access_Token);
        Long memberId = memberIdRe.get();
        MemberEntity memberEntity = memberRepository.findById(memberId).orElse(null);

        WebClient webClient = WebClient.builder()
                .baseUrl(kakaoUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + admin_key)
                .build();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("target_id_type", "user_id");
        requestBody.add("target_id", String.valueOf(memberEntity.getCertificationId()));
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/user/logout")
                        .build())
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .toBodilessEntity()
                .block();

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

        return kakaoUserInfoResponse;
    }


}
