package com.constant;

import lombok.Getter;

@Getter
public enum UserStatus {

    /**
     * Currently in use
     */
    ACTIVE("active"),

    /**
     * User account has been frozen
     */
    FROZEN("frozen"),

    /**
     *  User de-registered from the system
     */
    DEREG("dereg");

    private String status;

     UserStatus(String s){
        this.status=s;
    }

    @Override
    public String toString() {
        return status;
    }
}
