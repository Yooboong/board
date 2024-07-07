package com.yooboong.board.controller;

import com.yooboong.board.dto.CommentDto;
import com.yooboong.board.dto.PostingDto;
import com.yooboong.board.service.CommentService;
import com.yooboong.board.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor // lombok을 사용한 생성자 주입 어노테이션
@RequestMapping("/posting")
public class PostingController {

    private final PostingService postingService; // lombok을 사용한 생성자 주입시 final 추가
//    private final CommentService commentService;
    @GetMapping("/") // 게시판 전체 글 조회 (메인 페이지)
    public String readAll(Model model) {
        List<PostingDto> postingDtos = postingService.readAll();

        model.addAttribute("postingDtos", postingDtos);
        return "posting/mainpage";
    }

    @GetMapping("/new") // 글 생성 버튼 눌렀을 때
    public String toNew() {
        return "posting/new"; // 글 생성 페이지로 이동
    }

    @PostMapping("/create") // 글 생성
    public String create(@RequestParam("title") String title,
                         @RequestParam("content") String content) {

        PostingDto input = PostingDto.builder()
                .title(title)
                .content(content)
                .build();

        PostingDto created = postingService.create(input);

        return "redirect:/posting/";
    }

    @GetMapping("/{id}") // 게시글 조회
    public String show(@PathVariable("id") Long id,
                       Model model) {
        PostingDto postingDto = postingService.read(id);
//        List<CommentDto> commentDtos = commentService.readAll(id);

        model.addAttribute("postingDto", postingDto);
//        model.addAttribute("commentDto", commentDtos);
        return "posting/show";
    }

    @GetMapping("/{id}/edit") // 수정 시작
    public String edit(@PathVariable("id") Long id,
                       Model model) {
        // 수정할 데이터 가져오기
        PostingDto target = postingService.read(id);

        model.addAttribute("postingDto", target);
        return "/posting/edit";
    }

    @PostMapping("/{id}/update") // 수정
    public String update(@PathVariable("id") Long id,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content) {
        PostingDto input = PostingDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();

        PostingDto updated = postingService.update(id, input);

        return "redirect:/posting/" + id;
    }

    @GetMapping("/{id}/delete") // 삭제
    public String delete(@PathVariable("id") Long id) {
        postingService.delete(id);

        return "redirect:/posting/";
    }

}
