package com.context;

import com.entity.req.AuthorityApplyRequest;
import com.entity.req.UserLoginRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAccessCheckContext extends AccessCheckContext{


    UserLoginRequest userLoginRequest;

    // define this class for further reuse for specific login

}
