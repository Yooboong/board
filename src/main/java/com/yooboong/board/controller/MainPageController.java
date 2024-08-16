package com.yooboong.board.controller;

import com.yooboong.board.dto.BoardDto;
import com.yooboong.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final BoardService boardService;

    @GetMapping("/")
    public String toMainPage() {
        List<BoardDto> boardList = boardService.getBoardList();
        if (boardList.isEmpty())
            return "redirect:/login";

        return "redirect:/board/" + boardList.get(0).getId(); // 첫번째 게시판으로
    }
}
