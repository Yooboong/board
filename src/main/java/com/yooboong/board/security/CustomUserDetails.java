package com.yooboong.board.security;

import com.yooboong.board.dto.AccountDto;
import com.yooboong.board.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomUserDetails implements UserDetails {

    private final AccountDto accountDto;
    private final List<GrantedAuthority> authorities;

    // 일반 로그인 생성자
    public CustomUserDetails(AccountDto accountDto, List<GrantedAuthority> authorities) {
        this.accountDto = accountDto;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return accountDto.getPassword();
    }

    @Override
    public String getUsername() {
        return accountDto.getUsername();
    }

    // 뷰에 닉네임을 보여주기위해 추가
    public String getNickname() {
        return accountDto.getNickname();
    }

    // 닉네임 수정시 변경된 닉네임을 뷰에 반영하기위해 추가
    public void setNickname(String newNickname) {
        this.accountDto.setNickname(newNickname);
    }

}
