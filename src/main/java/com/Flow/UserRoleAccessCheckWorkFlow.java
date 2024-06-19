package com.Flow;

import com.constant.AuthDesc;
import com.constant.AuthException;
import com.context.AccessCheckContext;
import com.context.LoginAccessCheckContext;
import com.entity.DTO.User;
import com.entity.UserLogin;
import com.entity.resp.UserLoginAccessCheckResp;
import com.entity.req.UserLoginRequest;
import com.entity.req.UserLoginRequestWithSign;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class UserRoleAccessCheckWorkFlow {

    @Autowired
    UserService userService;

    public UserLoginAccessCheckResp doProcess(UserLoginRequestWithSign userLoginRequestWithSign) {

        LoginAccessCheckContext loginAccessCheckContext = new LoginAccessCheckContext();

        //check signature
        // system design that a user login with key encrpty its id
        // so the signature decrypt should same as it id
        // i just demostrate that theroy
        // in practice system keys are not symmetric in this situation
        // this system using symmetric way of SM4 alogrithum
        checkUserLoginRequestSignature(userLoginRequestWithSign, loginAccessCheckContext);

        //check user role whether valid
        // role -- admin user
        // a user try to access via role higher priority than its actual permission will detected
        // while an admin user try to access system with user role is acceptable
        Boolean userLoginCheckResult = checkUserRoleAndStatus(loginAccessCheckContext);

        //update user log-in time
        // db maintains last_login_time
        //if a user successfully access last_login_time shoud be update for access time
        // if a user is refused by system e.g try to access via admin role while it only have user permission
        // system will not update its last_login_time
        // db dont record login time this value only update in memory to build resp to terminal
        updateUserLoginTime(loginAccessCheckContext);

        // return resp
        // value of last_login_time in resp shows previous login_time in db
        // if user successfully access then last_login_time in db has already updated
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
        Boolean adminOnly = Optional.ofNullable(accessCheckContext.getAdminOnly()).orElse(Boolean.FALSE);
        User userInDb = userService.checkUserRole(userLogin, adminOnly);
        accessCheckContext.setUser(userInDb);

        if (Objects.isNull(userInDb)
                ||
                !StringUtils.equals(userLogin.getAccountName(),userInDb.getAccountName()))
            return Boolean.FALSE;
        Boolean isUserLoginCapable = userService.checkUserStatus(userInDb.getStatus());

        if (!isUserLoginCapable) {
            log.warn("user status not support login,accountName: {},userStatus: {}",
                    userInDb.getAccountName(), userInDb.getStatus());
        }
        accessCheckContext.setCheckResult(isUserLoginCapable);
        return isUserLoginCapable;
    }

    private void checkUserLoginRequestSignature(UserLoginRequestWithSign userLoginRequestWithSign,
                                                LoginAccessCheckContext loginAccessCheckContext) {
        buildLoginSignatureCheckContext(userLoginRequestWithSign, loginAccessCheckContext);
        try {
            userService.checkUserSignature(userLoginRequestWithSign.getSignature(),
                    userLoginRequestWithSign.getUserId());
        } catch (Exception e) {
            log.error("UserSignature check failed",e);
            throw new AuthException(AuthDesc.USER_SIGNATURE_INVALID,null);
        }
    }

    private void buildLoginSignatureCheckContext(UserLoginRequestWithSign userLoginRequestWithSign,
                                                 LoginAccessCheckContext loginAccessCheckContext) {
        loginAccessCheckContext.setUserLoginRequest(userLoginRequestWithSign);
        loginAccessCheckContext.setSignature(userLoginRequestWithSign.getSignature());
        UserLoginRequest standardReq = userLoginRequestWithSign;
        loginAccessCheckContext.setUserLogin(UserLogin.createUserLogin(standardReq));
    }
}
