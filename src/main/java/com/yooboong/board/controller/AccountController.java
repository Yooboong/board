package com.yooboong.board.controller;

import com.yooboong.board.dto.AccountDto;
import com.yooboong.board.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/signup")
    public String signup() {
        return "account/signup";
    }

    @PostMapping("/signup")
    public String signup(AccountDto accountDto) {
        AccountDto created = accountService.create(accountDto);
        return "redirect:/";
    }

}
