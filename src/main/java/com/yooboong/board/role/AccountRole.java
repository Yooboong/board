package com.yooboong.board.role;

import lombok.Getter;

@Getter
public enum AccountRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String value;

    AccountRole(String value) {
        this.value = value;
    }
}
