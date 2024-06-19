package com.constant;

public class AuthException extends RuntimeException{

    String resultCode;

    String resultDescription;

    public AuthException(AuthDesc pd){
        this.resultCode=pd.getResCode();
        this.resultDescription=pd.getResDesc();
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }
}
