package com.yooboong.board.service;

import com.yooboong.board.dto.AccountDto;
import com.yooboong.board.dto.CustomUserDetails;
import com.yooboong.board.entity.Account;
import com.yooboong.board.repository.AccountRepository;
import com.yooboong.board.role.AccountRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountSecurityService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (account.getPermit() == 1) { // permit가 1 일땐 관리자, 0 일땐 일반사용자
            authorities.add(new SimpleGrantedAuthority(AccountRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(AccountRole.USER.getValue()));
        }

//        return new User(account.getUsername(), account.getPassword(), authorities);
        AccountDto accountDto = AccountDto.toDto(account);
        return new CustomUserDetails(accountDto, authorities);
    }

}
