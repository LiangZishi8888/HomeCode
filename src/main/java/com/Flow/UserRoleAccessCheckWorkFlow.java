package com.Flow;

import com.constant.AuthDesc;
import com.constant.AuthException;
import com.context.AccessCheckContext;
import com.context.LoginAccessCheckContext;
import com.entity.DTO.User;
import com.entity.UserLogin;
import com.entity.resp.UserLoginAccessCheckResp;
import com.entity.req.UserLoginRequest;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class UserRoleAccessCheckWorkFlow {

    @Autowired
    UserService userService;

    public UserLoginAccessCheckResp doProcess(UserLoginRequest userLoginRequest) {

        LoginAccessCheckContext loginAccessCheckContext = new LoginAccessCheckContext();

        buildUserLoginContext(userLoginRequest, loginAccessCheckContext);

        //check user role whether valid
        // role -- admin user
        // a user try to access via role higher priority than its actual permission will detected
        // while an admin user try to access system with user role is acceptable
        Boolean userLoginCheckResult = checkUserRoleAndStatus(loginAccessCheckContext);

        //update user log-in time
        // db maintains last_login_time
        //if a user successfully access last_login_time shoud be update for current access time
        // a user is refused by system e.g try to access via admin role while it only have user permission
        // system will not update its last_login_time if user status is not ACTIVE
        // only useid username compatible with db will pass to the following process
        updateUserLoginTime(loginAccessCheckContext);

        // return resp
        // value of last_login_time in resp shows previous login_time in db
        // checkResp show success means user pass all check and login_time updated
        // if user status is not ACTIVE will not update

        return UserLoginAccessCheckResp.createAccessCheckResultResp(userLoginCheckResult,
                loginAccessCheckContext.getUserLogin());
    }


    private void updateUserLoginTime(LoginAccessCheckContext loginAccessCheckContext) {
        Objects.requireNonNull(loginAccessCheckContext.getCheckResult());
        UserLogin userLogin = loginAccessCheckContext.getUserLogin();
        if (loginAccessCheckContext.getCheckResult() == Boolean.TRUE)
            userService.updateUserLoginTime(userLogin);
    }

    public Boolean checkUserRoleAndStatus(AccessCheckContext accessCheckContext) {
        UserLogin userLogin=accessCheckContext.getUserLogin();
        Objects.requireNonNull(userLogin);

        //this propertiy will modify in previous stage buildContext in different work flow
        boolean adminOnly = Optional.ofNullable(accessCheckContext.getAdminOnly())
                .orElse(Boolean.FALSE);
        User userInDb = userService.checkUserRole(userLogin, adminOnly);
        List<User> contextUsers=Optional.ofNullable(accessCheckContext.getUsers())
                .orElseGet(ArrayList::new);
        contextUsers.add(userInDb);
        accessCheckContext.setUsers(contextUsers);

        // userInDb is null then will throw ex in line71
        // this is check account Name Consistency only in access interface
        // in grant interface this will skipp
        if (StringUtils.isNotEmpty(userLogin.getAccountName())&&
                !StringUtils.equals(userLogin.getAccountName(),userInDb.getAccountName())){
            log.error(" user id is not compatible with its accountName,id:{},accountName:{}",
                    userLogin.getUserId(),userLogin.getAccountName());
            throw new AuthException(AuthDesc.USER_NOT_EXIST,null);
        }
        Boolean isUserLoginCapable = userService.checkUserStatus(userInDb.getStatus());

        if (!isUserLoginCapable) {
            log.warn("user status not support login,accountName: {},userStatus: {}",
                    userInDb.getAccountName(), userInDb.getStatus());
        }

        //only valid userId and userName will execute this line and status not active return check res false
        accessCheckContext.setCheckResult(isUserLoginCapable);
        return isUserLoginCapable;
    }

    private void buildUserLoginContext(UserLoginRequest userLoginRequest,
                                       LoginAccessCheckContext loginAccessCheckContext) {
        loginAccessCheckContext.setUserLoginRequest(userLoginRequest);
        UserLoginRequest standardReq = userLoginRequest;
        loginAccessCheckContext.setUserLogin(UserLogin.createUserLogin(standardReq));
    }
}
