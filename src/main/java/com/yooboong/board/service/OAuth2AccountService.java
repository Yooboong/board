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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2AccountService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String accessToken = userRequest.getAccessToken().getTokenValue();

        String oAuth2Id = String.valueOf(attributes.get("id"));
        Account account = accountRepository.findByOAuth2Id(oAuth2Id);

        if (account == null) {
            account = Account.builder()
                    .oAuth2Id(oAuth2Id)
                    .accessToken(accessToken)
                    .email(oAuth2Id)
                    .username("kakao_" + oAuth2Id)
                    .password(oAuth2Id)
                    .nickname("kakao_" + oAuth2Id)
                    .permit(0)
                    .build();

            account = accountRepository.save(account);
        } else {
            accountRepository.updateAccessToken(oAuth2Id, accessToken);
            account = accountRepository.findByOAuth2Id(oAuth2Id);
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
