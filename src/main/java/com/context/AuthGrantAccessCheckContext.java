package com.context;

import com.constant.AuthCategory;

import java.util.List;

public class AuthGrantAccessCheckContext extends AccessCheckContext{

    List<AuthCategory> alreadyHoldAuths;

    List<AuthCategory> expectedGrantAuths;

}
