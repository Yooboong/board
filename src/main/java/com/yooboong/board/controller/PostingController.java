package com.yooboong.board.controller;

import com.yooboong.board.dto.CommentDto;
import com.yooboong.board.dto.PostingDto;
import com.yooboong.board.role.AccountRole;
import com.yooboong.board.service.CommentService;
import com.yooboong.board.service.PostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collection;
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
    public String createForm(PostingDto postingDto) {
        return "posting/new"; // 글 생성 페이지로 이동
    }

    @PreAuthorize("isAuthenticated()") // 로그인한 경우에만 실행, 로그인한 사용자만 호출가능
    // 로그아웃 상태에서 해당 어노테이션이 적용된 메소드가 호출되면, 로그인 페이지로 강제이동
    @PostMapping("/create") // 글 생성
    public String create(Principal principal,
                         @Valid PostingDto postingDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) { // 제목 및 내용을 작성하지 않은경우
            model.addAttribute("postingDto", postingDto);
            return "posting/new";
        }

        PostingDto input = PostingDto.builder()
                .title(postingDto.getTitle())
                .content(postingDto.getContent())
                .view(0)
                .build();

        PostingDto created = postingService.create(principal.getName(), input);

        return "redirect:/";
    }

    @GetMapping("/{id}") // 게시글 조회
    public String show(@PathVariable("id") Long id,
                       Model model) {
        PostingDto postingDto = postingService.read(id);

        postingService.increaseView(id); // 게시글 조회수 증가

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
        return "posting/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update") // 수정
    public String update(Principal principal,
                         @Valid PostingDto postingDto,
                         BindingResult bindingResult,
                         Model model) {
        PostingDto target = postingService.read(postingDto.getId());
        if (!target.getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 수정 권한이 없음");

        if (bindingResult.hasErrors()) { // 제목 및 내용이 비어있는경우
            model.addAttribute("postingDto", postingDto);
            return "posting/edit";
        }

        PostingDto input = PostingDto.builder()
                .id(postingDto.getId())
                .title(postingDto.getTitle())
                .content(postingDto.getContent())
                .build();

        PostingDto updated = postingService.update(postingDto.getId(), input);

        return "redirect:/posting/" + postingDto.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete") // 삭제
    public String delete(Principal principal,
                         @PathVariable("id") Long id) {
        PostingDto target = postingService.read(id);

        // 권한이 관리자인 경우 삭제 가능하도록 구현

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 관리자 여부 확인
        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority(AccountRole.ADMIN.getValue()));

        //

        if (!isAdmin && !target.getUsername().equals(principal.getName())) // 관리자가 아니면서 작성자 본인이 아닌경우
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 삭제 권한이 없음");

        postingService.delete(id);

        return "redirect:/";
    }

}
