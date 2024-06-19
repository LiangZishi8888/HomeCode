package com.controller;

import com.Flow.UserRoleAccessCheckWorkFlow;
import com.entity.req.AuthorityApplyRequet;
import com.entity.resp.BaseResp;
import com.entity.req.UserLoginRequestWithSign;
import com.entity.resp.UserLoginAccessCheckResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorityController {

    @Autowired
    UserRoleAccessCheckWorkFlow userRoleAccessCheckWorkFlow;

    @PostMapping("/role/access")
    public UserLoginAccessCheckResp checkUserAccess(@RequestBody UserLoginRequestWithSign userLoginRequestWithSign){
        return userRoleAccessCheckWorkFlow.doProcess(userLoginRequestWithSign);
    }

    //Unfinished
    @PostMapping("/admin/addUser")
    public BaseResp addUserResources(@RequestBody AuthorityApplyRequet authorityApplyRequet){
         return null;
    }
}
