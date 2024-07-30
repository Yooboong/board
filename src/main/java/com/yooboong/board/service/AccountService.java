package com.yooboong.board.service;

import com.yooboong.board.dto.AccountDto;
import com.yooboong.board.entity.Account;
import com.yooboong.board.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    public AccountDto create(AccountDto accountDto) {
        if (accountRepository.findByEmail(accountDto.getEmail()) != null) {
            throw new IllegalArgumentException("이미 사용중인 email 입니다");
        }
        if (accountRepository.findByUsername(accountDto.getUsername()) != null) {
            throw new IllegalArgumentException("이미 사용중인 아이디 입니다");
        }
        if (accountRepository.findByNickname(accountDto.getNickname()) != null) {
            throw new IllegalArgumentException("이미 사용중인 닉네임 입니다");
        }

        accountDto.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Account accountEntity = Account.toEntity(accountDto);

        Account created = accountRepository.save(accountEntity);

        AccountDto createdDto = AccountDto.toDto(created);
        return createdDto;
    }

}
