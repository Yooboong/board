package com.yooboong.board.dto;

import com.yooboong.board.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommentDto {
    private Long id;
    private String username; // Principal.getName()과 비교하기위함 (계정 아이디에 해당)
    private String nickname; // 작성자 닉네임
    private Long postingId;

    @NotBlank(message = "댓글을 입력하세요")
    private String comment;

    private String createdDate, modifiedDate;

    public static CommentDto toDto(Comment entity) {
        return CommentDto.builder()
                .id(entity.getId())
                .username(entity.getAuthor().getUsername())
                .nickname(entity.getAuthor().getNickname())
                .postingId(entity.getPosting().getId())
                .comment(entity.getComment())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }
}
