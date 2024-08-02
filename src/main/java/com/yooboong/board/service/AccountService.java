package com.yooboong.board.service;

import com.yooboong.board.dto.AccountDto;
import com.yooboong.board.entity.Account;
import com.yooboong.board.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AccountDto create(AccountDto accountDto) {
        accountDto.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Account accountEntity = Account.toEntity(accountDto);

        Account created = accountRepository.save(accountEntity);

        AccountDto createdDto = AccountDto.toDto(created);
        return createdDto;
    }

    public AccountDto getAccount(String username) {
        Account accountEntity = accountRepository.findByUsername(username);
        if (accountEntity == null)
            throw new IllegalArgumentException("회원정보 조회 실패! 해당하는 계정이 존재하지 않음");

        AccountDto accountDto = AccountDto.toDto(accountEntity);
        return accountDto;
    }

    @Transactional
    public AccountDto updateMyInfo(String username, AccountDto accountDto) {
        Account target = accountRepository.findByUsername(username);
        if (target == null)
            throw new IllegalArgumentException("회원정보 수정 실패! 해당하는 계정이 존재하지 않음");

        target.updateInfo(accountDto);
        Account updated = accountRepository.save(target);

        AccountDto updatedDto = AccountDto.toDto(updated);
        return updatedDto;
    }

    @Transactional
    public AccountDto updatePassword(String username, String password) {
        Account target = accountRepository.findByUsername(username);
        if (target == null)
            throw new IllegalArgumentException("비밀번호 변경 실패! 해당하는 계정이 존재하지 않음");

        target.updatePassword(passwordEncoder.encode(password)); // 새 패스워드 인코딩 후 업데이트
        Account updated = accountRepository.save(target);

        AccountDto updatedDto = AccountDto.toDto(updated);
        return updatedDto;
    }

    @Transactional
    public void delete(String username) {
        Account target = accountRepository.findByUsername(username);
        if (target == null)
            throw new IllegalArgumentException("계정 삭제 실패! 해당하는 계정이 존재하지 않음");

        accountRepository.delete(target);
    }

    public boolean checkDuplicateEmail(String email) { // 이미 가입된 이메일인지 체크
        Account target = accountRepository.findByEmail(email);
        if (target != null) {
            return true;
        }
        return false;
    }

    public boolean checkDuplicateUsername(String username) { // 사용중인 아이디인지 체크
        Account target = accountRepository.findByUsername(username);
        if (target != null) {
            return true;
        }
        return false;
    }

    public boolean checkDuplicateNickname(String nickname) { // 사용중인 닉네임인지 체크
        Account target = accountRepository.findByNickname(nickname);
        if (target != null) {
            return true;
        }
        return false;
    }

}
