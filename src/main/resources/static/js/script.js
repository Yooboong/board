// 페이지 번호 눌렀을 때
function changePage(page) {
    var pageNumber = page.getAttribute('data-page'); // 페이지 번호 가져오기

    document.getElementById('page').value = pageNumber; // searchForm의 <input type="hidden" name="page" id="page">에 페이지 번호 저장

    document.getElementById('searchForm').submit(); // 전송
}

// 검색어를 입력후 검색버튼을 눌렀을 때
function searchPosting() {
    // searchForm의 <input type="hidden" name="keyword" id="keyword">에 검색어 저장
    document.getElementById('keyword').value = document.getElementById('searchKeyword').value;

    document.getElementById('page').value = 1; // 검색 버튼을 클릭 시 첫번째 페이지로 세팅

    // 검색 옵션값 세팅
    document.getElementById('searchOption').value = document.getElementById('search-option').value;

    document.getElementById('searchForm').submit(); // 전송
}

// 게시글 작성시 validation
function postingValidation() {
    var title = document.getElementById('titleForm').value.trim();
    var content = document.getElementById('contentForm').value.trim();

    if (title == '') {
        alert('제목을 입력하세요');
        return false;
    }

    if (content == '') {
        alert('내용을 입력하세요');
        return false;
    }

    alert('글이 등록되었습니다');
    return true; // true 반환시 폼 제출 허용 (onsubmit 으로 submit 전에 실행됨)
}

// 게시글 수정시 validation
function postingEditValidation() {
    var title = document.getElementById('titleForm').value.trim();
    var content = document.getElementById('contentForm').value.trim();

    if (title == '') {
        alert('제목을 입력하세요');
        return false;
    }

    if (content == '') {
        alert('내용을 입력하세요');
        return false;
    }


    // 사용자가 수정 작업을 진행하기 전에 확인 알림을 표시 (confirm 함수 사용)
    var result = confirm('글을 수정하시겠습니까?');

    // 사용자가 아니오를 클릭하면 수정 작업을 취소
    if (result == false) {
        alert('수정이 취소되었습니다');
        return false; // 폼 제출 취소
    }

    alert('글이 수정되었습니다');
    return true; // true 반환시 폼 제출 허용 (onsubmit 으로 submit 전에 실행됨)
}

// 게시글 삭제
function confirmDeletePosting(postingId) {
    var result = confirm('글을 삭제하시겠습니까?');

    // 사용자가 아니오를 클릭한경우
    if (result == false) {
        alert('삭제가 취소되었습니다');
        return; // 아무 동작도 하지않음
    }

    // 사용자가 예를 클릭하면 삭제 작업 진행
    alert('글이 삭제되었습니다');
    location.href = '/posting/' + postingId + '/delete';
}

// 댓글 작성시 validation
function commentValidation(form) {
    var comment = form.querySelector('textarea[name="comment"]').value.trim();
    if (comment == '') {
        alert('댓글을 입력하세요');
        return false;
    }
    alert('댓글이 등록되었습니다');
    return true;
}

// 댓글 수정시 validation
function commentEditValidation(form) {
    var comment = form.querySelector('textarea[name="comment"]').value.trim();
    if (comment == '') {
        alert('댓글을 입력하세요');
        return false;
    }

    var result = confirm('댓글을 수정하시겠습니까?');
    if (result == false) {
        alert('수정이 취소되었습니다')
        return false;
    }

    alert('댓글이 수정되었습니다');
    return true;
}

// 댓글 수정시 수정폼 toggle
let openCommentEditForm = null; // 현재 열려 있는 댓글 수정 폼을 추적하기 위한 변수

function toggleCommentEditForm(button) {
    var card = button.closest('.card'); // 해당 버튼이 포함된 가장 가까운 .card 요소를 찾음
    var commentEditForm = card.querySelector('.comment-edit-form'); // 해당 .card 요소 내의 .comment-edit-form 요소를 찾음

    if (openCommentEditForm && openCommentEditForm !== commentEditForm) { // 현재 다른 댓글 수정 폼이 열려있을 때
        openCommentEditForm.querySelector('form').reset(); // 현재 열려있는 수정 폼 리셋
        openCommentEditForm.style.display = 'none'; // 현재 열려 있는 폼 숨기기
    }

    if (commentEditForm.style.display === 'none' || commentEditForm.style.display === '') {
        commentEditForm.style.display = 'block'; // 현재 폼 표시
        openCommentEditForm = commentEditForm; // 현재 폼을 추적
    } else { // 수정 취소하는 경우
        commentEditForm.querySelector('form').reset(); // 폼 리셋
        commentEditForm.style.display = 'none'; // 현재 폼 숨기기
        openCommentEditForm = null; // 현재 폼 추적 해제
    }
}

// 내정보 수정시 확인
function confirmEditAccount(form) {
    var nickname = form.querySelector('input[name="nickname"]').value.trim();

    if(nickname == '') {
        alert('닉네임을 입력하세요');
        return false;
    }

    var result = confirm('수정하시겠습니까?');
    if (result == false) {
        alert('수정이 취소되었습니다');
        return false;
    }

    return true;
}

// 비밀번호 수정시 확인
function confirmChangePassword(form) {
    var currentPassword = form.querySelector('input[name="currentPassword"]').value.trim();
    var password = form.querySelector('input[name="password"]').value.trim();
    var passwordConfirm = form.querySelector('input[name="passwordConfirm"]').value.trim();

    if (currentPassword == '') {
        alert('기존 비밀번호를 입력하세요');
        return false;
    }

    if (password == '') {
        alert('새 비밀번호를 입력하세요');
        return false;
    }

    if (passwordConfirm == '') {
        alert('비밀번호 확인을 입력하세요');
        return false;
    }

    var result = confirm('비밀번호를 변경하시겠습니까?');

    if (result == false) {
        alert('변경이 취소되었습니다');
        return false;
    }

    return true;
}

// 회원탈퇴시 확인
function confirmDeleteAccount(form) {
    var username = form.querySelector('input[name="username"]').value.trim();
    var password = form.querySelector('input[name="password"]').value.trim();

    if (username == '') {
        alert('아이디를 입력하세요');
        return false;
    }

    if(password == '') {
        alert('비밀번호를 입력하세요');
        return false;
    }

    var result = confirm('정말 탈퇴하시겠습니까?');

    if (result == false) {
        alert('탈퇴가 취소되었습니다');
        return false;
    }

    return true;
}