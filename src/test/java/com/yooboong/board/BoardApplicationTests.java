package com.yooboong.board;

import com.yooboong.board.dto.AccountDto;
import com.yooboong.board.dto.PostingDto;
import com.yooboong.board.entity.Account;
import com.yooboong.board.entity.Board;
import com.yooboong.board.entity.Posting;
import com.yooboong.board.repository.AccountRepository;
import com.yooboong.board.repository.BoardRepository;
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
    private BoardRepository boardRepository;

    @Autowired
    private PostingRepository postingRepository;

    @Test
    void testData() {
        // 관리자 계정 생성
        Account admin = Account.builder()
                .email("yooboong22@gmail.com")
                .username("admin")
                .password(passwordEncoder.encode("yooboong22"))
                .nickname("관리자")
                .permit(1)
                .build();

        admin = accountRepository.save(admin);

        // 게시판 생성
        Board board = Board.builder()
                .name("자유게시판")
                .build();

        board = boardRepository.save(board);

        for (int i = 1; i <= 150; i++) {
            String title = String.format("테스트 제목:[%03d]", i);
            String content = String.format("테스트 내용:[%03d]", i);

            PostingDto postingDto = PostingDto.builder()
                    .title(title)
                    .content(content)
                    .build();

            Posting posting = Posting.toEntity(admin, board, postingDto);

            postingRepository.save(posting);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
