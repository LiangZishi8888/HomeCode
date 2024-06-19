package com.context;

import com.entity.DTO.User;
import com.entity.UserLogin;
import com.entity.req.UserLoginRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAccessCheckContext extends AccessCheckContext{

    UserLoginRequest userLoginRequest;

    String signature;

}
