package com.yooboong.board.dto;

import com.yooboong.board.entity.Account;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountDto {
    private Long id;
    private String email;
    private String username;
    private String password;
    private String nickname;
    private Integer permit;

    public static AccountDto toDto(Account entity) {
        return AccountDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .permit(entity.getPermit())
                .build();
    }
}
