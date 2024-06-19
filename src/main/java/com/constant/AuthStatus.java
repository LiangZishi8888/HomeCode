package com.constant;

public enum AuthStatus {

    ACTIVE("active"),

    FORBIDDEN("forbidden"),

    DEREG("dereg");

    private String status;

    AuthStatus(String status) {
        this.status = status;
    }

}