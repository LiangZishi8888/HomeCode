package com.demo.context;

import com.demo.entity.UserLogin;
import com.demo.entity.req.UserLoginRequest;
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
     * the req map to userLogin
     */
    private UserLoginRequest userLoginRequest;

    // define this class for further reuse for specific login

}
