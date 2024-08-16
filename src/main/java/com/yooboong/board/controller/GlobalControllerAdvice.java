package com.yooboong.board.controller;

import com.yooboong.board.dto.BoardDto;
import com.yooboong.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice // 전역 컨트롤러
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final BoardService boardService;

    @ModelAttribute("boardList")
    public List<BoardDto> getBoardList(Model model) {
        List<BoardDto> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);
        return boardService.getBoardList();
    }

}
