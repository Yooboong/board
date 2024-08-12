package com.yooboong.board.controller;

import com.yooboong.board.dto.AccountDto;
import com.yooboong.board.oauth.CustomOAuth2User;
import com.yooboong.board.security.CustomUserDetails;
import com.yooboong.board.oauth.KakaoToken;
import com.yooboong.board.oauth.KakaoUserInfo;
import com.yooboong.board.service.AccountService;
import com.yooboong.board.service.KakaoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final KakaoService kakaoService;

//    @GetMapping("/login/oauth2/code/kakao") // 인가코드 받기
//    @ResponseBody
//    public String kakaoCallback(@RequestParam(name = "code") String code,
//                                Model model) {
//        // 카카오에서 반환한 인가코드 받아서 토큰을 요청
//        KakaoToken kakaoToken = kakaoService.requestToken(code);
//
//        // Access Token 으로 사용자 정보 요청
//        KakaoUserInfo kakaoUserInfo = kakaoService.requestUserInfo(kakaoToken.getAccess_token());
//
//        return kakaoUserInfo.toString();
//    }

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
        if (!accountDto.getPasswordConfirm().trim().equals("") && !accountDto.getPassword().equals(accountDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordMismatch", "비밀번호가 일치하지 않습니다");
        }

        if (bindingResult.hasFieldErrors("email") || bindingResult.hasFieldErrors("username") || bindingResult.hasFieldErrors("nickname") || bindingResult.hasFieldErrors("password") || bindingResult.hasFieldErrors("passwordConfirm")) {
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
    public String showMyInfo(@AuthenticationPrincipal UserDetails userDetails, // 일반 로그인일때만 유효하도록
                             Model model) {
        AccountDto accountDto = accountService.getAccount(userDetails.getUsername());

        model.addAttribute("accountDto", accountDto);
        return "account/info";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account/oauth2")
    public String showOAuth2MyInfo(@AuthenticationPrincipal OAuth2User oAuth2User, // OAuth2 로그인일때만 유효하도록
                                   Model model) {
        AccountDto accountDto = accountService.getOAuth2Account(oAuth2User.getName());
        model.addAttribute("accountDto", accountDto);
        return "account/oauth2info";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account/update") // 비밀번호를 제외한 내정보 수정
    public String updateMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
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

        AccountDto updated = accountService.updateMyInfo(userDetails.getUsername(), accountDto);

//        // 업데이트된 닉네임을 뷰에 보여주는 로직 (해당 로직은 Principal 사용시 유효)
//
//        // 현재 인증된 사용자의 Authentication 객체를 가져옴
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // 인증된 사용자의 세부 정보를 반환(UserDetails 인터페이스를 구현한 객체)
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        // UserDetails 인터페이스를 구현한 CustomUserDetails에 추가로 구현한 setNickname 메소드로 변경된 닉네임 반영
//        ((CustomUserDetails) userDetails).setNickname(updated.getNickname());
//
//        // 업데이트된 정보로 세션 업데이트
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        userDetails.setNickname(updated.getNickname()); // @AuthenticationPrincipal 사용하여 세션에 변경사항 바로 적용

        return "redirect:/account";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account/oauth2/update")
    public String updateOAuth2MyInfo(@AuthenticationPrincipal CustomOAuth2User oAuth2User,
                                     @Valid AccountDto accountDto,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasFieldErrors("nickname")) {
            model.addAttribute("accountDto", accountDto);
            return "account/oauth2info";
        }

        if (accountService.checkDuplicateNickname(accountDto.getNickname())) { // 이미 사용중인 닉네임을 작성한경우
            bindingResult.rejectValue("nickname", "duplicateNickname", "이미 사용중인 닉네임 입니다");
            model.addAttribute("accountDto", accountDto);
            return "account/oauth2info";
        }

        AccountDto updated = accountService.updateOAuth2MyInfo(oAuth2User.getName(), accountDto);

        oAuth2User.setNickname(updated.getNickname());

        return "redirect:/account/oauth2";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account/password") // 비밀번호 수정 페이지
    public String editPasswordForm(@AuthenticationPrincipal UserDetails userDetails, AccountDto accountDto) {
        return "account/password";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account/password/update") // 비밀번호 수정
    public String updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @Valid AccountDto accountDto,
                                 BindingResult bindingResult,
                                 Model model) {
        // 기존 비밀번호가 틀린경우
        if (!accountDto.getCurrentPassword().trim().equals("") && !accountService.checkCurrentPassword(userDetails.getUsername(), accountDto.getCurrentPassword())) {
            bindingResult.rejectValue("currentPassword", "currentPasswordMismatch", "비밀번호가 일치하지 않습니다");
        }

        // 새 비밀번호와 비밀번호 확인이 일치하지 않는경우 (새 비밀번호가 일치하지 않습니다)
        if (!accountDto.getPassword().equals(accountDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "newPasswordMismatch", "새 비밀번호가 일치하지 않습니다");
        }

        // 문제가 생긴경우 (+ 그 외)
        // *기존 비밀번호를 입력하지 않은경우
        // *새 비밀번호의 형식이 맞지 않는경우
        // *비밀번호 확인을 입력하지 않은경우
        if (bindingResult.hasFieldErrors("currentPassword") || bindingResult.hasFieldErrors("password") || bindingResult.hasFieldErrors("passwordConfirm")) {
            model.addAttribute("accountDto", accountDto);
            return "account/password";
        }

        AccountDto updated = accountService.updatePassword(userDetails.getUsername(), accountDto.getPassword());

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account/delete") // 회원탈퇴 페이지
    public String deleteForm(@AuthenticationPrincipal UserDetails userDetails, AccountDto accountDto) {
        return "account/delete";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account/delete") // 회원탈퇴
    public String delete(@AuthenticationPrincipal UserDetails userDetails,
                         @Valid AccountDto accountDto,
                         BindingResult bindingResult,
                         Model model) {
        // 아이디가 일치하지 않는경우
        if (!accountDto.getUsername().trim().equals("") && !userDetails.getUsername().equals(accountDto.getUsername())) {
            bindingResult.rejectValue("username", "usernameMismatch", "아이디가 일치하지 않습니다");
        }

        // 비밀번호가 일치하지 않는경우
        if (!accountDto.getPassword().trim().equals("") && !accountService.checkCurrentPassword(userDetails.getUsername(), accountDto.getPassword())) {
            bindingResult.rejectValue("password", "passwordMismatch", "비밀번호가 일치하지 않습니다");
        }

        // 문제가 생긴경우 (+ 그 외)
        // 아이디를 입력하지 않았거나 형식에 맞지 않는경우
        // 비밀번호를 입력하지 않았거나 형식에 맞지 않는경우
        if (bindingResult.hasFieldErrors("username") || bindingResult.hasFieldErrors("password")) {
            model.addAttribute("accountDto", accountDto);
            return "account/delete";
        }

        accountService.delete(userDetails.getUsername());

        return "redirect:/logout"; // 탈퇴 후 로그아웃으로 세션삭제
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account/oauth2/delete")
    @ResponseBody
    public ResponseEntity deleteOAuth2(@AuthenticationPrincipal OAuth2User oAuth2User) { // 카카오로그인 회원탈퇴
        AccountDto target = accountService.getOAuth2Account(oAuth2User.getName());
        String accessToken = target.getAccessToken(); // accessToken 값 저장

        accountService.deleteOAuth2(oAuth2User.getName()); // DB 부터 삭제
        String unlinkResult = kakaoService.unlinkOAuth2(accessToken); // 카카오 unlink

//        return "redirect:/logout"; // 로그아웃으로 세션 삭제
        return ResponseEntity.ok(unlinkResult);
    }

}
