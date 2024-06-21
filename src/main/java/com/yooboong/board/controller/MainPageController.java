package com.yooboong.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {
    @GetMapping("/")
    public String toMainPage() {
        return "redirect:/posting/";
    }
}
