package com.yooboong.board;

import com.yooboong.board.entity.Posting;
import com.yooboong.board.repository.PostingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private PostingRepository postingRepository;

    @Test
    void testData() {
        for (int i = 1; i <= 250; i++) {
            String title = String.format("테스트 제목:[%03d]", i);
            String content = String.format("테스트 내용:[%03d]", i);

            Posting posting = Posting.builder()
                    .title(title)
                    .content(content)
                    .build();

            postingRepository.save(posting);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
