package com.yooboong.board.entity;

import com.yooboong.board.dto.PostingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor // JPA는 기본생성자로 객체를 생성하므로 반드시 선언
@AllArgsConstructor
@Builder
@Getter
public class Posting extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    public static Posting toEntity(PostingDto postingDto) {
        return Posting.builder()
                .id(postingDto.getId())
                .title(postingDto.getTitle())
                .content(postingDto.getContent())
                .build();
    }

    public void update(PostingDto postingDto) { // 게시글 수정
        this.title = postingDto.getTitle();
        this.content = postingDto.getContent();
    }
}
