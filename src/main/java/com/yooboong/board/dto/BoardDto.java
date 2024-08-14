package com.yooboong.board.dto;

import com.yooboong.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardDto {
    private Long id;

    @NotBlank(message = "게시판 이름을 입력하세요")
    private String name;

    public static BoardDto toDto(Board entity) {
        return BoardDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
