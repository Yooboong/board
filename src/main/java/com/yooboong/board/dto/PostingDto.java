package com.yooboong.board.dto;

import com.yooboong.board.entity.Comment;
import com.yooboong.board.entity.Posting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
public class PostingDto {
    private Long id;
    private String title;
    private String content;

    private List<CommentDto> commentDtoList;

    public static PostingDto toDto(Posting entity) {
        // List<Comment> 를 List<CommentDto>로 변환
        List<Comment> commentList = entity.getCommentList();
        List<CommentDto> commentDtos = null;

        if (commentList != null) {
            commentDtos = entity.getCommentList().stream()
                    .map(comment -> CommentDto.toDto(comment))
                    .collect(Collectors.toList());
        }

        return PostingDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .commentDtoList(commentDtos)
                .build();
    }
}
