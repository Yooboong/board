package com.yooboong.board.controller;

import com.yooboong.board.dto.CommentDto;
import com.yooboong.board.dto.PostingDto;
import com.yooboong.board.service.CommentService;
import com.yooboong.board.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor // lombok을 사용한 생성자 주입 어노테이션
@RequestMapping("/posting")
public class PostingController {

    private final PostingService postingService; // lombok을 사용한 생성자 주입시 final 추가

    @GetMapping("/") // 메인 페이지 (페이징)
    public String mainPage(@RequestParam(name = "page", defaultValue = "1") int page,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword,
                           @RequestParam(name = "searchOption", defaultValue = "1") int searchOption,
                           Model model) {
        Page<PostingDto> postingDtoPage = postingService.getPage(page, keyword, searchOption);

        int startPage = ((page - 1) / postingDtoPage.getSize()) * postingDtoPage.getSize() + 1;
        int endPage = startPage + postingDtoPage.getSize() - 1;
        if (endPage > postingDtoPage.getTotalPages()) {
            endPage = postingDtoPage.getTotalPages();
        }
        int currentPage = page;
        int lastPage = postingDtoPage.getTotalPages();

        model.addAttribute("postingDtoPage", postingDtoPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("searchOption", searchOption);
        return "mainpage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new") // 글 생성 버튼 눌렀을 때
    public String createForm() {
        return "posting/new"; // 글 생성 페이지로 이동
    }

    @PreAuthorize("isAuthenticated()") // 로그인한 경우에만 실행, 로그인한 사용자만 호출가능
    // 로그아웃 상태에서 해당 어노테이션이 적용된 메소드가 호출되면, 로그인 페이지로 강제이동
    @PostMapping("/create") // 글 생성
    public String create(Principal principal,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content) {

        PostingDto input = PostingDto.builder()
                .title(title)
                .content(content)
                .view(0)
                .build();

        PostingDto created = postingService.create(principal.getName(), input);

        return "redirect:/posting/";
    }

    @GetMapping("/{id}") // 게시글 조회
    public String show(@PathVariable("id") Long id,
                       Model model) {
        PostingDto postingDto = postingService.read(id);

        postingService.increaseView(id);

        model.addAttribute("postingDto", postingDto);
        return "posting/show";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit") // 수정 시작
    public String editForm(Principal principal,
                           @PathVariable("id") Long id,
                           Model model) {
        // 수정할 데이터 가져오기
        PostingDto target = postingService.read(id);

        if (!target.getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 수정 권한이 없음");

        model.addAttribute("postingDto", target);
        return "/posting/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update") // 수정
    public String update(Principal principal,
                         @RequestParam("id") Long id,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content) {
        PostingDto target = postingService.read(id);
        if (!target.getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 수정 권한이 없음");

        PostingDto input = PostingDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();

        PostingDto updated = postingService.update(id, input);

        return "redirect:/posting/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete") // 삭제
    public String delete(Principal principal,
                         @PathVariable("id") Long id) {
        PostingDto target = postingService.read(id);
        if (!target.getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 삭제 권한이 없음");

        postingService.delete(id);

        return "redirect:/posting/";
    }

}
