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

// 게시글 작성, 수정시 validation
function postingValidation() {
    var title = document.getElementById('titleForm').value.trim();
    var content = document.getElementById('contentForm').value.trim();

    var flag = true; // validation flag

    if (title == '') {
        alert('제목을 입력하세요');
        flag = false;
    }

    if (content == '') {
        alert('내용을 입력하세요');
        flag = false;
    }

    return flag; // flag가 true면 폼 제출 허용 (onsubmit 으로 submit 전에 실행됨)
}

// 댓글 작성, 수정시 validation
function commentValidation(form) {
    var comment = form.querySelector('textarea[name="comment"]').value.trim();
    if (comment == '') {
        alert('댓글을 입력하세요');
        return false;
    }
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