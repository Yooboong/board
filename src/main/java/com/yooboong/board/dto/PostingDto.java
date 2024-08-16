package com.yooboong.board.dto;

import com.yooboong.board.entity.Comment;
import com.yooboong.board.entity.Posting;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostingDto {
    private Long id;
    private Long boardId;

    private String username; // Principal.getName()과 비교하기위함 (계정 아이디에 해당)
    private String nickname; // 작성자 닉네임

    @NotBlank(message = "제목을 입력하세요")
    private String title;

    @NotBlank(message = "내용을 입력하세요")
    private String content;

    private Integer view;

    private List<CommentDto> commentDtoList;

    private String createdDate, modifiedDate;

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
                .boardId(entity.getBoard().getId())
                .username(entity.getAuthor().getUsername())
                .nickname(entity.getAuthor().getNickname())
                .title(entity.getTitle())
                .content(entity.getContent())
                .view(entity.getView())
                .commentDtoList(commentDtos)
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }
}
