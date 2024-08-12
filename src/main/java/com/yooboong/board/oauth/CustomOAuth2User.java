package com.yooboong.board.oauth;

import com.yooboong.board.dto.AccountDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private AccountDto accountDto;
    private Map<String,Object> attributes;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2User(AccountDto accountDto, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        this.accountDto = accountDto;
        this.attributes = attributes;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
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
