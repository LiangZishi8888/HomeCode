package com.controller;

import com.Flow.AdminGrantAccessWorkFlow;
import com.Flow.UserRoleAccessCheckWorkFlow;
import com.entity.crypto.DecryptRequest;
import com.entity.req.AuthorityApplyRequest;
import com.entity.req.UserLoginRequest;
import com.entity.resp.BaseResp;
import com.entity.resp.UserLoginAccessCheckResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthorityController {

    @Autowired
    UserRoleAccessCheckWorkFlow userRoleAccessCheckWorkFlow;

    @Autowired
    AdminGrantAccessWorkFlow adminGrantAccessWorkFlow;

    @PostMapping("/role/access")
    @DecryptRequest
    public BaseResp<UserLoginAccessCheckResp> checkUserAccess(@RequestBody UserLoginRequest userLoginRequest){
        log.info(userLoginRequest.toString());
        return userRoleAccessCheckWorkFlow.doProcess(userLoginRequest);
    }

    @PostMapping("/admin/addUser")
    public BaseResp addUserResources(@RequestBody AuthorityApplyRequest authorityApplyRequet){
        //return adminGrantAccessWorkFlow.doProcess(authorityApplyRequet);
        return null;
    }
}
