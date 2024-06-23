package com.demo.controller;

import com.demo.Flow.AdminGrantAccessWorkFlow;
import com.demo.Flow.QueryUserGrantWorkFlow;
import com.demo.Flow.UserRoleAccessCheckWorkFlow;
import com.demo.entity.crypto.DecryptRequest;
import com.demo.entity.req.AuthorityApplyRequest;
import com.demo.entity.req.UserLoginRequest;
import com.demo.entity.resp.AuthGrantResp;
import com.demo.entity.resp.QueryResp;
import com.demo.entity.resp.UserLoginAccessCheckResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class AuthorityController {

    @Autowired
    UserRoleAccessCheckWorkFlow userRoleAccessCheckWorkFlow;

    @Autowired
    AdminGrantAccessWorkFlow adminGrantAccessWorkFlow;

    @Autowired
    QueryUserGrantWorkFlow queryUserGrantWorkFlow;

    @PostMapping("/role/access")
    @DecryptRequest
    public UserLoginAccessCheckResp checkUserAccess(@RequestBody UserLoginRequest userLoginRequest){
        return userRoleAccessCheckWorkFlow.doProcess(userLoginRequest);
    }

    @PostMapping("/admin/addUser")
    public AuthGrantResp addUserResources(@RequestBody AuthorityApplyRequest authorityApplyRequet){
        return adminGrantAccessWorkFlow.doProcess(authorityApplyRequet);
    }

    @GetMapping("/query/user")
    public QueryResp queryUserAuths(@RequestParam String userId, @RequestParam String authName){
         return queryUserGrantWorkFlow.doProcess(userId,authName);
    }
}
