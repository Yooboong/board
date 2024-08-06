package com.yooboong.board.dto;

import com.yooboong.board.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final AccountDto accountDto;
    private final List<GrantedAuthority> authorities;

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
