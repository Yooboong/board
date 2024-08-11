package com.yooboong.board.service;

import com.yooboong.board.dto.AccountDto;
import com.yooboong.board.entity.Account;
import com.yooboong.board.oauth.CustomOAuth2User;
import com.yooboong.board.repository.AccountRepository;
import com.yooboong.board.role.AccountRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2AccountService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String id = String.valueOf(attributes.get("id"));
        Account account = accountRepository.findByTokenId(id);

        if (account == null) {
            account = Account.builder()
                    .tokenId(id)
                    .permit(0)
                    .build();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (account.getPermit() == 1) { // permit가 1 일땐 관리자, 0 일땐 일반사용자
            authorities.add(new SimpleGrantedAuthority(AccountRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(AccountRole.USER.getValue()));
        }

        AccountDto accountDto = AccountDto.toDto(account);

        return new CustomOAuth2User(accountDto, attributes, authorities);
    }

}
