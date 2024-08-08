package com.yooboong.board.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yooboong.board.oauth.KakaoToken;
import lombok.RequiredArgsConstructor;
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

    private final String REST_API_KEY = "f057e561048b05be1c0bef3deea37919";
    private final String REDIRECT_URI = "http://127.0.0.1:8080/auth/kakao/callback";

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
    public String requestUserInfo(String accessToken) {
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

        return response.getBody();
    }

}
