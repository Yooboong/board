package com.yooboong.board.controller;

import com.yooboong.board.dto.BoardDto;
import com.yooboong.board.dto.PostingDto;
import com.yooboong.board.service.BoardService;
import com.yooboong.board.service.PostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    private final PostingService postingService; // lombok을 사용한 생성자 주입시 final 추가

//    @GetMapping("/{id}") // 게시판 메인 페이지 (페이징)
//    public String mainPage(@PathVariable("id") Long id,
//                           @RequestParam(name = "page", defaultValue = "1") int page,
//                           @RequestParam(name = "keyword", defaultValue = "") String keyword,
//                           @RequestParam(name = "searchOption", defaultValue = "1") int searchOption,
//                           Model model) {
//        Page<PostingDto> postingDtoPage = postingService.getPage(id, page, keyword, searchOption);
//
//        int startPage = ((page - 1) / postingDtoPage.getSize()) * postingDtoPage.getSize() + 1;
//        int endPage = startPage + postingDtoPage.getSize() - 1;
//        if (endPage > postingDtoPage.getTotalPages()) {
//            endPage = postingDtoPage.getTotalPages();
//        }
//        int currentPage = page;
//        int lastPage = postingDtoPage.getTotalPages();
//
//        model.addAttribute("boardId", id);
//        model.addAttribute("postingDtoPage", postingDtoPage);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//        model.addAttribute("currentPage", currentPage);
//        model.addAttribute("lastPage", lastPage);
//        model.addAttribute("searchOption", searchOption);
//        return "mainpage";
//    }

    @GetMapping("/manage") // 게시판 관리버튼 눌렀을 때
    public String manageForm(Model model) {
        List<BoardDto> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);
        return "board/manage";
    }

    @GetMapping("/new") // 생성버튼 눌렀을 때
    public String createForm(BoardDto boardDto) {
        return "board/new";
    }

    @PostMapping("/create") // 생성
    public String create(@Valid BoardDto boardDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasFieldErrors("name")) {
            model.addAttribute("boardDto", boardDto);
            return "board/new";
        }

        if (boardService.checkDuplicateName(boardDto.getName())) { // 게시판 이름 중복일 때
            bindingResult.rejectValue("name", "boardNameDuplicated", "해당 게시판이 이미 존재합니다");
            model.addAttribute("boardDto", boardDto);
            return "board/new";
        }

        BoardDto created = boardService.create(boardDto);

        return "redirect:/board/manage";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id,
                           Model model) { // 수정버튼 눌렀을 때
        BoardDto target = boardService.getBoard(id);
        model.addAttribute("boardDto", target);
        return "board/edit";
    }

    @PostMapping("/update") // 생성
    public String update(@Valid BoardDto boardDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasFieldErrors("name")) {
            model.addAttribute("boardDto", boardDto);
            return "board/edit";
        }

        if (boardService.checkDuplicateName(boardDto.getName())) { // 게시판 이름 중복일 때
            bindingResult.rejectValue("name", "boardNameDuplicated", "해당 게시판이 이미 존재합니다");
            model.addAttribute("boardDto", boardDto);
            return "board/edit";
        }

        BoardDto updated = boardService.update(boardDto);

        return "redirect:/board/manage";
    }

    @PostMapping("/{id}/delete")
    @ResponseBody
    public ResponseEntity delete(@PathVariable("id") Long id) {
        boardService.delete(id);
        return ResponseEntity.ok(null);
    }

}
