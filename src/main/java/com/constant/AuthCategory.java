package com.constant;

import lombok.Getter;

@Getter
public enum AuthCategory {

    ALL("all"),

    AUTHA("autha"),

    AUTHB("authb"),

    AUTHC("authc");

    private String name;

    AuthCategory(String status) {
        this.name = status;
    }
}
