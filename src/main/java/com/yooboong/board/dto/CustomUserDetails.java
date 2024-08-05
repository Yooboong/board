package com.yooboong.board.dto;

import com.yooboong.board.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Account account;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(Account account, List<GrantedAuthority> authorities) {
        this.account = account;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    public String getNickname() {
        return account.getNickname();
    }

}
