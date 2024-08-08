const REST_API_KEY = 'f057e561048b05be1c0bef3deea37919';
const REDIRECT_URI = 'http://127.0.0.1:8080/auth/kakao/callback';

function kakaoLogin() {
    var kakaoLoginUrl =
        'https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=' +
        REST_API_KEY +
        '&redirect_uri=' +
        REDIRECT_URI;

    location.href = kakaoLoginUrl;
}

