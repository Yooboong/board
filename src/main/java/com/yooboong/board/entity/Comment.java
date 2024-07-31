package com.yooboong.board.entity;

import com.yooboong.board.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Account author;

    @ManyToOne
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @Column(nullable = false)
    private String comment;

    public static Comment toEntity(Account account, Posting posting, CommentDto commentDto) {
        return Comment.builder()
                .author(account)
                .posting(posting)
                .comment(commentDto.getComment())
                .build();
    }

    public void update(CommentDto commentDto) {
        this.comment = commentDto.getComment();
    }
}
