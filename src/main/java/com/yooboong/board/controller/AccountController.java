package com.yooboong.board.controller;

import com.yooboong.board.dto.AccountDto;
import com.yooboong.board.entity.Account;
import com.yooboong.board.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String signupForm(AccountDto accountDto) { // th:object로 뷰에 바인딩 시키기위해 매개변수 추가
        return "account/signup";
    }

    @PostMapping("/signup") // 회원가입
    public String signup(@Valid AccountDto accountDto,
                         BindingResult bindingResult,
                         Model model) {
        // 이메일 중복 체크 추가
        if (accountService.checkDuplicateEmail(accountDto.getEmail())) {
            bindingResult.rejectValue("email", "duplicateEmail", "이미 사용중인 email 입니다");
        }

        // 아이디 중복 체크 추가
        if (accountService.checkDuplicateUsername(accountDto.getUsername())) {
            bindingResult.rejectValue("username", "duplicateUsername", "이미 사용중인 아이디 입니다");
        }

        // 닉네임 중복 체크 추가
        if (accountService.checkDuplicateNickname(accountDto.getNickname())) {
            bindingResult.rejectValue("nickname", "duplicateNickname", "이미 사용중인 닉네임 입니다");
        }

        // 비밀번호 확인 추가
        if (!accountDto.getPasswordConfirm().equals("") && !accountDto.getPassword().equals(accountDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordMismatch", "비밀번호가 일치하지 않습니다");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("accountDto", accountDto);
            return "account/signup";
        }

        AccountDto created = accountService.create(accountDto);

        return "redirect:/";
    }

    @GetMapping("/login") // 로그인 페이지
    public String loginForm() {
        return "account/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account") // 내 정보
    public String showMyInfo(Principal principal,
                             Model model) {
        AccountDto accountDto = accountService.getAccount(principal.getName());

        model.addAttribute("accountDto", accountDto);
        return "account/info";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account/update") // 비밀번호를 제외한 내정보 수정
    public String updateMyInfo(Principal principal,
                               @Valid AccountDto accountDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasFieldErrors("nickname")) {
            model.addAttribute("accountDto", accountDto);
            return "account/info";
        }

        if (accountService.checkDuplicateNickname(accountDto.getNickname())) { // 이미 사용중인 닉네임을 작성한경우
            bindingResult.rejectValue("nickname", "duplicateNickname", "이미 사용중인 닉네임 입니다");
            model.addAttribute("accountDto", accountDto);
            return "account/info";
        }

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
