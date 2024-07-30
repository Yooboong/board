package com.yooboong.board.repository;

import com.yooboong.board.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
    Account findByUsername(String username);
    Account findByNickname(String nickname);
}
