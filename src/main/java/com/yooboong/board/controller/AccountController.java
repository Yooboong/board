package com.yooboong.board.controller;

import com.yooboong.board.dto.AccountDto;
import com.yooboong.board.entity.Account;
import com.yooboong.board.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/signup") // 회원가입 페이지
    public String signupForm() {
        return "account/signup";
    }

    @PostMapping("/signup") // 회원가입
    public String signup(AccountDto accountDto) {
        AccountDto created = accountService.create(accountDto);

        return "redirect:/";
    }

    @GetMapping("/login") // 로그인 페이지
    public String loginForm() {
        return "account/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account") // 내 정보
    public String showAccountInfo(Principal principal,
                                  Model model) {
        AccountDto accountDto = accountService.getAccount(principal.getName());

        model.addAttribute("accountDto", accountDto);
        return "account/info";
    }

    // 닉네임 중복체크 구현할 것

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account/update") // 비밀번호를 제외한 내정보 수정
    public String updateMyInfo(Principal principal,
                               AccountDto accountDto,
                               Model model) {
        AccountDto updated = accountService.updateMyInfo(principal.getName(), accountDto);

        return "redirect:/account";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account/password") // 비밀번호 수정 페이지
    public String editPasswordForm() {
        return "account/password";
    }

    // 비밀번호 수정시, 기존 비밀번호 체크, 새 비밀번호, 비밀번호확인 구현할 것

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account/password/update") // 비밀번호 수정
    public String updatePassword(Principal principal,
                                 @RequestParam("password") String password) {
        AccountDto updated = accountService.updatePassword(principal.getName(), password);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account/delete") // 회원탈퇴 페이지
    public String deleteForm() {
        return "account/delete";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account/delete") // 회원탈퇴
    public String delete(Principal principal) { // 추가 검증 구현할 것
        accountService.delete(principal.getName());

        return "redirect:/logout"; // 탈퇴 후 로그아웃으로 세션삭제
    }

}
