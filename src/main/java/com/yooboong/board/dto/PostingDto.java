package com.yooboong.board.dto;

import com.yooboong.board.entity.Posting;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostingDto {
    private Long id;
    private String title;
    private String content;

    public static PostingDto toDto(Posting entity) {
        return new PostingDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContent()
        );
    }
}
