package com.yooboong.board.repository;

import com.yooboong.board.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
    Account findByUsername(String username);
    Account findByNickname(String nickname);

    @Query("select a " +
            "from Account a " +
            "where a.oAuth2Id = :oAuth2Id")
    Account findByOAuth2Id(@Param("oAuth2Id") String oAuth2Id);

    @Modifying
    @Query("update Account a " +
            "set a.accessToken = :accessToken " +
            "where a.oAuth2Id = :oAuth2Id")
    void updateAccessToken(@Param("oAuth2Id") String oAuth2Id, @Param("accessToken") String accessToken);
}
