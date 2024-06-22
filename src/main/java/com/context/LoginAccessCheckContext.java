package com.context;

import com.entity.UserLogin;
import com.entity.req.AuthorityApplyRequest;
import com.entity.req.UserLoginRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAccessCheckContext extends AccessCheckContext{

    private Boolean checkResult;

    private UserLogin userLogin;

    private UserLoginRequest userLoginRequest;

    // define this class for further reuse for specific login

}
