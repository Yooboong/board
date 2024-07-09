package com.yooboong.board.entity;

import com.yooboong.board.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @Column
    private String comment;

    public static Comment toEntity(Posting posting, CommentDto commentDto) {
        return Comment.builder()
                .posting(posting)
                .comment(commentDto.getComment())
                .build();
    }

    public void update(CommentDto commentDto) {
        this.comment = commentDto.getComment();
    }
}
