package com.yooboong.board.entity;

import com.yooboong.board.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @Column
    private String comment;

    public static Comment toEntity(Posting posting, CommentDto commentDto) {
        if (posting.getId() != commentDto.getPostingId())
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 일치하지 않음");
        if (commentDto.getId() != null)
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 함");

        return new Comment(
                commentDto.getId(),
                posting,
                commentDto.getComment()
        );
    }

    public void patch(CommentDto commentDto) {
        if (commentDto.getId() != this.id)
            throw new IllegalArgumentException("댓글 수정 실패! 수정할 댓글의 id가 일치하지 않음");

        if (commentDto.getComment() != this.comment)
            this.comment = commentDto.getComment();
    }
}
