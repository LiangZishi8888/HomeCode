package com.context;

import com.entity.UserLogin;
import com.entity.req.AuthorityApplyRequest;
import com.entity.req.UserLoginRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * context of userlogin to access this system
 */
public class LoginAccessCheckContext extends AccessCheckContext{

    /**
     * access check result of user
     */
    private Boolean checkResult;

    /**
     * map some information for user access and adapt to workflow
     */
    private UserLogin userLogin;

    /**
     * the req map to userLogin
     */
    private UserLoginRequest userLoginRequest;

    // define this class for further reuse for specific login

}
