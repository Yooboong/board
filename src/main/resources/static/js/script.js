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