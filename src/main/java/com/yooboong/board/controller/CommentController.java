package com.yooboong.board.controller;

import com.yooboong.board.dto.CommentDto;
import com.yooboong.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posting/{postingId}/create")
    public String create(@PathVariable("postingId") Long postingId,
                         @RequestParam("comment") String comment) {
        CommentDto input = CommentDto.builder()
                .postingId(postingId)
                .comment(comment)
                .build();

        CommentDto created = commentService.create(postingId, input);
        return "redirect:/posting/" + postingId;
    }

    @GetMapping("/comment/{id}/edit")
    public String edit(@PathVariable("id") Long id,
                       Model model) {
        CommentDto commentDto = commentService.read(id);

        model.addAttribute("commentDto", commentDto);
        return "comments/edit";
    }

    @PostMapping("/comment/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @RequestParam("postingId") Long postingId,
                         @RequestParam("comment") String comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(id)
                .postingId(postingId)
                .comment(comment)
                .build();

        CommentDto updated = commentService.update(id, commentDto);

        return "redirect:/posting/" + postingId;
    }

    @GetMapping("/comment/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        Long deletedCommentPostingId = commentService.delete(id);

        return "redirect:/posting/" + deletedCommentPostingId;
    }

}
