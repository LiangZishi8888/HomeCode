package com.demo.Flow;

import com.demo.constant.AuthDesc;
import com.demo.constant.AuthException;
import com.demo.constant.UserRole;
import com.demo.context.LoginAccessCheckContext;
import com.demo.dao.UserDao;
import com.demo.entity.DTO.UserDTO;
import com.demo.entity.UserLogin;
import com.demo.entity.resp.UserLoginAccessCheckResp;
import com.demo.entity.req.UserLoginRequest;
import com.demo.service.UserService;
import com.demo.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class UserRoleAccessCheckWorkFlow {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

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


    public Boolean checkUserRoleAndStatus(LoginAccessCheckContext accessCheckContext) {
        UserLogin userLogin = accessCheckContext.getUserLogin();
        Objects.requireNonNull(userLogin);

        //this propertiy will modify in previous stage buildContext in different work flow
        boolean adminOnly = Optional.ofNullable(accessCheckContext.getAdminOnly())
                .orElse(Boolean.FALSE);
        UserDTO userInDb = userService.checkUserRole(userLogin, adminOnly);
        userLogin.setStatus(userInDb.getStatus());

        // userInDb is null then will throw ex in line71
        // this is check account Name Consistency only in access interface
        // in grant interface this will skipp
        if (StringUtils.isNotEmpty(userLogin.getAccountName()) &&
                !StringUtils.equals(userLogin.getAccountName(), userInDb.getAccountName())) {
            log.error(" user id is not compatible with its accountName,id:{},accountName:{}",
                    userLogin.getUserId(), userLogin.getAccountName());
            throw new AuthException(AuthDesc.USER_NOT_EXIST, userLogin.getUserId());
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
        loginAccessCheckContext.setAccessTime(DateUtils.getCurrentDate());
        loginAccessCheckContext.setUserLogin(UserLogin.createUserLogin(userLoginRequest));
        if (StringUtils.equals(userLoginRequest.getRole(), UserRole.ADMIN.getRole()))
            loginAccessCheckContext.setAdminOnly(Boolean.TRUE);
        else
            loginAccessCheckContext.setAdminOnly(Boolean.FALSE);
    }
}
