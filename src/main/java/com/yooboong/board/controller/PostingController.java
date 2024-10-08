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
@RequestMapping("/board")
public class PostingController {

    private final PostingService postingService; // lombok을 사용한 생성자 주입시 final 추가

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{boardId}/new") // 글 생성 버튼 눌렀을 때
    public String createForm(@PathVariable("boardId") Long boardId,
                             PostingDto postingDto,
                             Model model) {
        model.addAttribute("boardId", boardId);
        return "posting/new"; // 글 생성 페이지로 이동
    }

    @PreAuthorize("isAuthenticated()") // 로그인한 경우에만 실행, 로그인한 사용자만 호출가능
    // 로그아웃 상태에서 해당 어노테이션이 적용된 메소드가 호출되면, 로그인 페이지로 강제이동
    @PostMapping("/{boardId}/create") // 글 생성
    public String create(Principal principal,
                         @PathVariable("boardId") Long boardId,
                         @Valid PostingDto postingDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) { // 제목 및 내용을 작성하지 않은경우
            model.addAttribute("boardId", boardId);
            model.addAttribute("postingDto", postingDto);
            return "posting/new";
        }

        PostingDto input = PostingDto.builder()
                .boardId(boardId)
                .title(postingDto.getTitle())
                .content(postingDto.getContent())
                .view(0)
                .build();

        PostingDto created = postingService.create(principal.getName(), input);

        return "redirect:/board/" + boardId;
    }

    @GetMapping("/{boardId}/posting/{id}") // 게시글 조회
    public String show(@PathVariable("id") Long id,
                       Model model) {
        PostingDto postingDto = postingService.read(id);

        postingService.increaseView(id); // 게시글 조회수 증가

        model.addAttribute("postingDto", postingDto);
        return "posting/show";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{boardId}/posting/{id}/edit") // 수정 시작
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
    @PostMapping("/{boardId}/posting/update") // 수정
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
                .boardId(postingDto.getBoardId())
                .title(postingDto.getTitle())
                .content(postingDto.getContent())
                .build();

        PostingDto updated = postingService.update(postingDto.getId(), input);

        return "redirect:/board/" + postingDto.getBoardId() + "/posting/" + postingDto.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{boardId}/posting/{id}/delete") // 삭제
    public String delete(Principal principal,
                         @PathVariable("boardId") Long boardId,
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

        return "redirect:/board/" + boardId;
    }

}
