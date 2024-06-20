package com.constant;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{

    String resultCode;

    String resultDescription;

    String errorMsg;

    public AuthException(AuthDesc pd,String errorMsg){
        this.resultCode=pd.getResCode();
        this.resultDescription=pd.getResDesc();
        this.errorMsg=errorMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }
}
