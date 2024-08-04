package com.yooboong.board.controller;

import com.yooboong.board.dto.CommentDto;
import com.yooboong.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(Principal principal,
                         @RequestParam("postingId") Long postingId,
                         @RequestParam("comment") String comment) {
        CommentDto input = CommentDto.builder()
                .postingId(postingId)
                .comment(comment)
                .build();

        CommentDto created = commentService.create(principal.getName(), postingId, input);
        return "redirect:/posting/" + postingId;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public String update(Principal principal,
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

        return "redirect:/posting/" + postingId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String delete(Principal principal,
                         @PathVariable("id") Long id) {
        CommentDto target = commentService.read(id);
        if (!target.getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 삭제 권한이 없음");

        Long deletedCommentPostingId = commentService.delete(id);

        return "redirect:/posting/" + deletedCommentPostingId;
    }

}