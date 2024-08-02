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

    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "이메일 형식이 맞지 않습니다")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])[a-z0-9]+$", message = "아이디는 알파벳 소문자로 시작하며, 알파벳 소문자와 숫자만 작성할 수 있습니다.")
//    @NotBlank(message = "아이디를 입력하세요")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+={}|\\:;\"'<>,.?/~`-]+$", message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자만 포함할 수 있으며 공백은 허용되지 않습니다.")
//    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;

    @Pattern(regexp = "^\\S+$", message = "닉네임에 공백은 포함할 수 없습니다")
    //    @NotBlank(message = "닉네임을 입력하세요")
    private String nickname;

    private Integer permit;

    @NotBlank(message = "비밀번호를 확인하세요")
    private String passwordConfirm;

    private String currentPassword;

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
