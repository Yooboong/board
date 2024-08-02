package com.yooboong.board;

import com.yooboong.board.dto.AccountDto;
import com.yooboong.board.dto.PostingDto;
import com.yooboong.board.entity.Account;
import com.yooboong.board.entity.Posting;
import com.yooboong.board.repository.AccountRepository;
import com.yooboong.board.repository.PostingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BoardApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostingRepository postingRepository;

    @Test
    void testData() {
        AccountDto accountDto = AccountDto.builder()
                .email("yooboong22@gmail.com")
                .username("admin")
                .password(passwordEncoder.encode("yooboong22"))
                .nickname("관리자")
                .build();

        Account account = accountRepository.save(Account.toEntity(accountDto));

        for (int i = 1; i <= 250; i++) {
            String title = String.format("테스트 제목:[%03d]", i);
            String content = String.format("테스트 내용:[%03d]", i);

            PostingDto postingDto = PostingDto.builder()
                    .title(title)
                    .content(content)
                    .build();

            Posting posting = Posting.toEntity(account, postingDto);

            postingRepository.save(posting);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
