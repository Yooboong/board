package com.yooboong.board.controller;

import com.yooboong.board.dto.CommentDto;
import com.yooboong.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    public String create(@RequestParam("postingId") Long postingId,
                         @RequestParam("comment") String comment) {
        CommentDto input = CommentDto.builder()
                .postingId(postingId)
                .comment(comment)
                .build();

        CommentDto created = commentService.create(postingId, input);
        return "redirect:/posting/" + postingId;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id,
                       Model model) {
        CommentDto commentDto = commentService.read(id);

        model.addAttribute("commentDto", commentDto);
        return "comments/edit";
    }

    @PostMapping("/update")
    public String update(@RequestParam("id") Long id,
                         @RequestParam("comment") String comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(id)
                .comment(comment)
                .build();

        CommentDto updated = commentService.update(id, commentDto);

        Long postingId = updated.getPostingId();

        return "redirect:/posting/" + postingId;
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        Long deletedCommentPostingId = commentService.delete(id);

        return "redirect:/posting/" + deletedCommentPostingId;
    }

}
