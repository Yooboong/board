package com.yooboong.board.dto;

import com.yooboong.board.entity.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountDto {
    private Long id;

    private String oAuth2Id;

    private String accessToken;

    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String email;

    @NotBlank(message = "아이디를 입력하세요")
    @Pattern(regexp = "^[a-z0-9]{5,20}$", message = "아이디는 5~20자의 알파벳 소문자, 숫자만 사용가능합니다")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+={}\\[\\]|\\\\;:'\",.<>?/~`]{8,16}$", message = "비밀번호는 8~16자의 알파벳 대소문자, 숫자, 특수문자만 사용가능합니다")
    private String password;

    @NotBlank(message = "닉네임을 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,20}$", message = "닉네임은 2~20자의 알파벳 대소문자, 숫자, 한글만 사용가능합니다")
    private String nickname;

    private Integer permit;

    @NotBlank(message = "비밀번호를 확인하세요")
    private String passwordConfirm;

    @NotBlank(message = "기존 비밀번호를 입력하세요")
    private String currentPassword;

    public static AccountDto toDto(Account entity) {
        return AccountDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .permit(entity.getPermit())
                .oAuth2Id(entity.getOAuth2Id())
                .accessToken(entity.getAccessToken())
                .build();
    }
}
