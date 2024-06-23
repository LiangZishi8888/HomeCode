package com.constant;

import lombok.Getter;

@Getter
public enum AuthCategory {

    ALL("all"),

    AUTHA("authA"),

    AUTHB("authB"),

    AUTHC("authC");

    private String name;

    AuthCategory(String status) {
        this.name = status;
    }
}
