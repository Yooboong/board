package com.yooboong.board.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yooboong.board.oauth.KakaoToken;
import com.yooboong.board.oauth.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoService {

    // @Value는 application.properties에 있는 값을 가져오기위한 어노테이션
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String REST_API_KEY;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String CLIENT_SECRET;

    // 카카오에서 반환한 인가코드 받아서 토큰을 요청
    public KakaoToken requestToken(String code) {
        // header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", REST_API_KEY);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);
        body.add("client_secret", CLIENT_SECRET);

        // header + body
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

        // http 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token", // 요청할 서버 주소
                HttpMethod.POST, // post 방식 요청
                httpEntity, // 요청시 보낼 데이터
                String.class // 요청시 반환되는 데이터타입
        );

        // JSON 데이터를 객체로 변환하기 위해 ObjectMapper 사용
        ObjectMapper objectMapper = new ObjectMapper();

        KakaoToken kakaoToken = null;
        try {
            kakaoToken = objectMapper.readValue(response.getBody(), KakaoToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return kakaoToken;
    }

    // Access Token 값으로 사용자 정보 요청하기
    public KakaoUserInfo requestUserInfo(String accessToken) {
        // header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpEntity 구성
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        // http 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me", // 요청할 서버 주소
                HttpMethod.POST, // post 방식 요청
                httpEntity, // 요청시 보낼 데이터
                String.class // 요청시 반환되는 데이터타입
        );

        // JSON 데이터를 객체로 변환하기 위해 ObjectMapper 사용
        ObjectMapper objectMapper = new ObjectMapper();

        KakaoUserInfo kakaoUserInfo = null;
        try {
            kakaoUserInfo = objectMapper.readValue(response.getBody(), KakaoUserInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return kakaoUserInfo;
    }

    public String unlinkOAuth2(String accessToken) { // 카카오 연결끊기 (서비스 탈퇴)
        // header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        // http 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/unlink", // 요청할 서버 주소
                HttpMethod.POST, // post 방식 요청
                httpEntity, // 요청시 보낼 데이터
                String.class // 요청시 반환되는 데이터타입
        );

        return response.getBody();
    }

}
