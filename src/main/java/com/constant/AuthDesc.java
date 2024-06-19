package com.constant;

public enum AuthDesc {

    SUCCESS("000000","Success"),

    DATA_TRANSFORM_ERROR("AUT0001", "Json_Convert_Error"),

    DB_INTERNAL_ERROR("AUT0002","Db_Internal_Error"),

    USER_SIGNATURE_INVALID("AUT0003","User_Signature_Invalid"),

    USER_ROLE_ERROR("AUT0004","User_Role_Failed"),

    USER_NOT_EXIST("AUT0005","User_Not_Exist"),

    UNOWN_USER_STATUS("AUT0006","Unown_User_Status");


    public String resCode;

    public String resDesc;

    public String getResCode() {
        return resCode;
    }

    public String getResDesc() {
        return resDesc;
    }

    AuthDesc(String resCode, String resDesc) {
        this.resCode = resCode;
        this.resDesc = resDesc;
    }
}
