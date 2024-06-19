package com.Flow;

import com.constant.AuthDesc;
import com.constant.AuthException;
import com.context.AuthGrantAccessCheckContext;
import com.entity.AuthCategoryEntity;
import com.entity.DTO.User;
import com.entity.GrantUserLogin;
import com.entity.UserLogin;
import com.entity.req.AuthorityApplyRequest;
import com.service.UserService;
import com.util.AuthCategoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AdminGrantAccessWorkFlow {

    @Autowired
    UserService userService;

    public void doProcess(@RequestBody AuthorityApplyRequest authorityApplyRequest){
        AuthGrantAccessCheckContext authGrantAccessCheckContext=new AuthGrantAccessCheckContext();

        buildAuthGrantAccessCheckContext( authorityApplyRequest,authGrantAccessCheckContext);

        //check whether the auths apply is valid
        preCheckAuthsValidation(authGrantAccessCheckContext.getExpectedGrantAuths());

        // check adminUser and User
        checkUsersStatusAndRole(authorityApplyRequest,authGrantAccessCheckContext);

        checkUserAuthApplyStatusIfhold()

    }

    private void buildAuthGrantAccessCheckContext(AuthorityApplyRequest authorityApplyRequest,
         AuthGrantAccessCheckContext authGrantAccessCheckContext){
         authGrantAccessCheckContext.setAdminOnly(Boolean.TRUE);
         authGrantAccessCheckContext.setExpectedGrantAuths(authorityApplyRequest.getExpectGrants()
                 .stream().map(auth->new AuthCategoryEntity(auth)).collect(Collectors.toList()));
         authGrantAccessCheckContext.setUsersInfo(
                 GrantUserLogin.createGrantUserLogin(authorityApplyRequest));
    }

    public void preCheckAuthsValidation(List<AuthCategoryEntity> expectAuths){
        for(AuthCategoryEntity auth:expectAuths){
            if(!AuthCategoryUtils.isValidAuth(auth.getAuthName())) {
                // we dont expose authName or we can expose for security depending
                // if dont expose just record log
                log.error("invalid auth application ,authName: ",auth.getAuthName());
                throw new AuthException(AuthDesc.INVALID_AUTH_APPLY, null);
            }
        }
    }

    public void checkUsersStatusAndRole(AuthorityApplyRequest authorityApplyRequest,
                                        AuthGrantAccessCheckContext accessCheckContext){
        UserLogin adminUserLogin=GrantUserLogin.buildUserLogin(
                accessCheckContext.getUsersInfo(),Boolean.TRUE);
        UserLogin toBeGrantedUserLogin=GrantUserLogin.buildUserLogin(
                accessCheckContext.getUsersInfo(),Boolean.FALSE);

        // if the user is not admin will throw exception in service method
        User adminUser = userService.checkUserRole(adminUserLogin, true);

        //same as previous if check fail will throw exception
        User grantUser=userService.checkUserRole(toBeGrantedUserLogin,false);

        boolean adminUserStatus=userService.checkUserStatus(adminUser.getStatus());
        boolean grantUserStatus=userService.checkUserStatus(grantUser.getStatus());

        // if userStatus not active we cant grant auth
        if(adminUserStatus&&grantUserStatus) {
            String userId=adminUserStatus?grantUser.getUserId():adminUser.getUserId();
            log.error("User status is not active, userId: "+userId);
            throw new AuthException(AuthDesc.USER_STATUS_NOT_ACTIVE,userId);
        }
    }
}
