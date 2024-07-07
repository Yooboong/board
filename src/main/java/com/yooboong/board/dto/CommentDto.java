package com.yooboong.board.dto;

import com.yooboong.board.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CommentDto {
    private Long id;
    private Long postingId;
    private String comment;
    
    public static CommentDto toDto(Comment entity) {
        return CommentDto.builder()
                .id(entity.getId())
                .postingId(entity.getPosting().getId())
                .comment(entity.getComment())
                .build();
    }
}
