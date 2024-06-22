package com.entity.resp;

import com.constant.AuthDesc;
import com.entity.UserLogin;
import lombok.*;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginAccessCheckResp extends BaseResp {

    private Boolean checkResult;

    private Date lastLoginTime;

    private Date loginTime;

    private String userStatus;

    public static UserLoginAccessCheckResp createAccessCheckResultResp(Boolean accessCheckResult, UserLogin userLogin){
        UserLoginAccessCheckResp userLoginAccessCheckResp= UserLoginAccessCheckResp.builder()
                .checkResult(accessCheckResult)
                .lastLoginTime(userLogin.getLastLoginTime())
                .userStatus(userLogin.getStatus().getStatus())
                .loginTime(userLogin.getLoginTime())
                .build();
        userLoginAccessCheckResp.setResultCode(AuthDesc.SUCCESS.getResCode());
        userLoginAccessCheckResp.setResultDescription(AuthDesc.SUCCESS.getResDesc());
        return userLoginAccessCheckResp;
    }
}
