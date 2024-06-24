package com.yooboong.board.dto;

import com.yooboong.board.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDto {
    private Long id;
    private Long postingId;
    private String comment;
    
    public static CommentDto toDto(Comment entity) {
        return new CommentDto(
                entity.getId(),
                entity.getPosting().getId(),
                entity.getComment()
        );
    }
}
