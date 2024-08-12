function kakaoLogout() { // 카카오 로그아웃
    var REST_API_KEY = 'f057e561048b05be1c0bef3deea37919';
    var LOGOUT_REDIRECT_URI = 'http://localhost:8080';

    var uri = 'https://kauth.kakao.com/oauth/logout?client_id='
    + REST_API_KEY
    +'&logout_redirect_uri='
    + LOGOUT_REDIRECT_URI;

    window.location.href = uri;
}

function ClearSessionAndKakaoLogout() { // 로그아웃으로 세션 삭제 후, 카카오 로그아웃
    fetch('/logout', { method: 'GET' })
        .then(response => {
            if (response.ok) {
                kakaoLogout();
            }
        })
        .catch(error => console.error('Logout failed:', error));
}

function deleteAndKakaoUnlink() { // 회원탈퇴 (DB삭제, unlink 후 logout)
    var result = confirm("정말 탈퇴하시겠습니까?")

    if (result == false) {
        window.alert('탈퇴가 취소되었습니다')
        return;
    }

    fetch('/account/oauth2/delete', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                window.alert('탈퇴되었습니다');
                window.location.href = '/logout'; // 로그아웃으로 세션 삭제
            }
        })
        .catch(error => console.error('Unlink failed:', error));
}