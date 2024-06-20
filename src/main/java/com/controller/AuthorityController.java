package com.controller;

import com.Flow.AdminGrantAccessWorkFlow;
import com.Flow.UserRoleAccessCheckWorkFlow;
import com.entity.req.AuthorityApplyRequest;
import com.entity.req.UserLoginRequest;
import com.entity.resp.BaseResp;
import com.entity.resp.UserLoginAccessCheckResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorityController {

    @Autowired
    UserRoleAccessCheckWorkFlow userRoleAccessCheckWorkFlow;

    @Autowired
    AdminGrantAccessWorkFlow adminGrantAccessWorkFlow;

    @PostMapping("/role/access")
    public UserLoginAccessCheckResp checkUserAccess(@RequestBody UserLoginRequest userLoginRequest){
        return userRoleAccessCheckWorkFlow.doProcess(userLoginRequest);
    }

    @PostMapping("/admin/addUser")
    public BaseResp addUserResources(@RequestBody AuthorityApplyRequest authorityApplyRequet){
        //return adminGrantAccessWorkFlow.doProcess(authorityApplyRequet);
        return null;
    }
}
