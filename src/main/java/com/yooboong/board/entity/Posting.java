package com.yooboong.board.entity;

import com.yooboong.board.dto.PostingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor // JPA는 기본생성자로 객체를 생성하므로 반드시 선언
@AllArgsConstructor
@Getter
public class Posting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    public static Posting toEntity(PostingDto postingDto) {
        if (postingDto.getId() != null) // 게시글 생성시 id는 null이어야 함
            throw new IllegalArgumentException("게시글 생성시 id가 null이 아님");

        return new Posting(
                postingDto.getId(),
                postingDto.getTitle(),
                postingDto.getContent());
    }

    public void patch(PostingDto postingDto) { // 게시글 수정
        if (postingDto.getId() != this.id)
            throw new IllegalArgumentException("수정할 게시글의 id가 일치하지 않음");
        if (postingDto.getTitle() != null)
            this.title = postingDto.getTitle();
        if (postingDto.getContent() != null)
            this.content = postingDto.getContent();
    }
}
