package com.demo.entity.resp;

import com.demo.constant.AuthDesc;
import com.demo.entity.UserLogin;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.demo.util.DateUtils;
import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
/**
 * resp entity class of
 * @see com.demo.Flow.UserRoleAccessCheckWorkFlow
 */
public class UserLoginAccessCheckResp extends BaseResp {

    /**
     * access result :
     *          true system has record access behaviour
     *          false system did not record access
     */
    private Boolean accessResult;

    /**
     * previous loginTime means user is exists in the system
     */
    @JsonFormat(pattern = DateUtils.DAY_MILLISECONDS,timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * loginTime means user currently loginTime
     */
    @JsonFormat(pattern=DateUtils.DAY_MILLISECONDS,timezone = "GMT+8")
    private Date loginTime;

    /**
     *  status of user
     * @see com.demo.constant.UserStatus
     */
    private String userStatus;

    public static UserLoginAccessCheckResp createAccessCheckResultResp(Boolean accessCheckResult, UserLogin userLogin){
        UserLoginAccessCheckResp userLoginAccessCheckResp= UserLoginAccessCheckResp.builder()
                .accessResult(accessCheckResult)
                .lastLoginTime(userLogin.getLastLoginTime())
                .userStatus(userLogin.getStatus().getStatus())
                .build();
        if(!accessCheckResult) {
            userLoginAccessCheckResp.setLoginTime(userLogin.getLastLoginTime());
            userLoginAccessCheckResp.setResultCode(AuthDesc.USER_STATUS_NOT_ACTIVE.getResCode());
            userLoginAccessCheckResp.setResultDescription(AuthDesc.USER_STATUS_NOT_ACTIVE.getResDesc());
        }else{
            userLoginAccessCheckResp.setLoginTime(userLogin.getLoginTime());
            userLoginAccessCheckResp.setResultCode(AuthDesc.SUCCESS.getResCode());
            userLoginAccessCheckResp.setResultDescription(AuthDesc.SUCCESS.getResDesc());
        }
        return userLoginAccessCheckResp;
    }
}
