package com.yooboong.board.controller;

import com.yooboong.board.dto.CommentDto;
import com.yooboong.board.role.AccountRole;
import com.yooboong.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(Principal principal,
                         @RequestParam("boardId") Long boardId,
                         @RequestParam("postingId") Long postingId,
                         @RequestParam("comment") String comment) {
        CommentDto input = CommentDto.builder()
                .postingId(postingId)
                .comment(comment)
                .build();

        CommentDto created = commentService.create(principal.getName(), postingId, input);
        return "redirect:/board/" + boardId + "/posting/" + postingId;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public String update(Principal principal,
                         @RequestParam("boardId") Long boardId,
                         @RequestParam("id") Long id,
                         @RequestParam("comment") String comment) {
        CommentDto target = commentService.read(id);
        if (!target.getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 수정 권한이 없음");

        CommentDto input = CommentDto.builder()
                .id(id)
                .comment(comment)
                .build();

        CommentDto updated = commentService.update(id, input);

        Long postingId = updated.getPostingId();

        return "redirect:/board/" + boardId + "/posting/" + postingId;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    @ResponseBody
    public ResponseEntity delete(Principal principal,
                                 @PathVariable("id") Long id) {
        CommentDto target = commentService.read(id);

        // 권한이 관리자인 경우 삭제 가능하도록 구현

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 관리자 여부 확인
        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority(AccountRole.ADMIN.getValue()));

        //

        if (!isAdmin && !target.getUsername().equals(principal.getName())) // 관리자가 아니면서 댓글 작성자가 아닌경우
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 삭제 권한이 없음");

        Long deletedCommentPostingId = commentService.delete(id);

        Map<String, Long> response = new HashMap<>(); // 삭제한 댓글의 게시글 id를 JSON타입으로 반환하기 위해 선언
        response.put("deletedCommentPostingId", deletedCommentPostingId); // 삭제한 댓글의 게시글 id를 넣어줌

        return ResponseEntity.ok(response);
    }

}