package com.yooboong.board.entity;

import com.yooboong.board.dto.AccountDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Account extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username; // 사용자 아이디

    @Column(nullable = false)
    private String password; // 사용자 비밀번호

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private Integer permit; // 권한 0 : 일반사용자, 1 : 관리자

    @Column(unique = true)
    private String oAuth2Id;

    @Column
    private String accessToken;

    public static Account toEntity(AccountDto accountDto) {
        return Account.builder()
                .email(accountDto.getEmail())
                .username(accountDto.getUsername())
                .password(accountDto.getPassword())
                .nickname(accountDto.getNickname())
                .permit(0) // 생성시 일반사용자 권한으로
                .oAuth2Id(accountDto.getOAuth2Id())
                .accessToken(accountDto.getAccessToken())
                .build();
    }

    public void updateInfo(AccountDto accountDto) {
        this.nickname = accountDto.getNickname();
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
