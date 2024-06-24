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
        CommentDto input = new CommentDto(
                null,
                postingId,
                comment
        );

        CommentDto created = commentService.create(postingId, input);
        return "redirect:/posting/" + created.getPostingId();
    }

    @GetMapping("/comment/{id}/edit")
    public String edit(@PathVariable("id") Long id,
                       Model model) {
        CommentDto commentDto = commentService.read(id);

        model.addAttribute("commentDto", commentDto);
        return "comments/edit";
    }

    @PostMapping("/comment/update")
    public String update(@RequestParam("id") Long id,
                         @RequestParam("postingId") Long postingId,
                         @RequestParam("comment") String comment) {
        CommentDto commentDto = new CommentDto(
                id,
                postingId,
                comment
        );

        CommentDto updated = commentService.update(id, commentDto);

        return "redirect:/posting/" + updated.getPostingId();
    }

    @GetMapping("/comment/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        CommentDto deleted = commentService.delete(id);

        return "redirect:/posting/" + deleted.getPostingId();
    }

}
