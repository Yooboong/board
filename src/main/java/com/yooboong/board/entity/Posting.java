package com.yooboong.board.entity;

import com.yooboong.board.dto.PostingDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Account author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer view;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    public static Posting toEntity(Account account, PostingDto postingDto) {
        return Posting.builder()
                .author(account)
                .title(postingDto.getTitle())
                .content(postingDto.getContent())
                .view(0)
                .build();
    }

    public void update(PostingDto postingDto) { // 게시글 수정
        this.title = postingDto.getTitle();
        this.content = postingDto.getContent();
    }
}