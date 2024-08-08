package com.yooboong.board.dto;

import com.yooboong.board.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomUserDetails implements UserDetails, OAuth2User {

    private final AccountDto accountDto;
    private final List<GrantedAuthority> authorities;

    private Map<String, Object> attributes; // OAuth2 소셜로그인을 위한 추가

    // 일반 로그인 생성자
    public CustomUserDetails(AccountDto accountDto, List<GrantedAuthority> authorities) {
        this.accountDto = accountDto;
        this.authorities = authorities;
    }

    // OAuth2 로그인 생성자
    public CustomUserDetails(AccountDto accountDto, List<GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.accountDto = accountDto;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() { // OAuth2 소셜로그인을 위한 추가
        return this.attributes;
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

    @Override
    public String getName() { // OAuth2 소셜로그인을 위한 추가
        return null;
    }
}
