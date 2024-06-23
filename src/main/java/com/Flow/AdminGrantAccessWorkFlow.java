package com.Flow;

import com.constant.AuthDesc;
import com.constant.AuthException;
import com.context.AuthGrantAccessCheckContext;
import com.entity.AuthCategoryEntity;
import com.entity.DTO.AuthDTO;
import com.entity.DTO.UserDTO;
import com.entity.GrantUserLogin;
import com.entity.UserLogin;
import com.entity.req.AuthorityApplyRequest;
import com.entity.resp.AuthGrantResp;
import com.service.AuthService;
import com.service.UserService;
import com.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AdminGrantAccessWorkFlow {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    public AuthGrantResp doProcess(@RequestBody AuthorityApplyRequest authorityApplyRequest) {

        AuthGrantAccessCheckContext authGrantAccessCheckContext = new AuthGrantAccessCheckContext();

        buildAuthGrantAccessCheckContext(authorityApplyRequest, authGrantAccessCheckContext);

        //check whether the auths apply is valid,the apply auths must all accept by system
        preCheckExpectAuthsValidation(authGrantAccessCheckContext.getExpectedGrantAuths());

        // check adminUser and User
        checkUsersStatusAndRole(authGrantAccessCheckContext.getUsersInfo());

        //will query in db and wrap to context
        checkUserExistsAuthStatus(authGrantAccessCheckContext.getExpectedGrantAuths(),
                authGrantAccessCheckContext.getUsersInfo());

        dispatchAuthsAndUpdate(authGrantAccessCheckContext);

        return  createGrantAuthSucessResp(authGrantAccessCheckContext.getUsersInfo(),
                authGrantAccessCheckContext);
    }

    private void buildAuthGrantAccessCheckContext(AuthorityApplyRequest authorityApplyRequest,
                                                  AuthGrantAccessCheckContext authGrantAccessCheckContext) {
        authGrantAccessCheckContext.setAdminOnly(Boolean.TRUE);
        authGrantAccessCheckContext.setAccessTime(DateUtils.getCurrentDate());
        // map expectGrants to entity and set to context
        authGrantAccessCheckContext.setExpectedGrantAuths(
                authorityApplyRequest.getExpectGrants()
                        .stream()
                        // casue expectAuths dont check we assume user not hold each one
                        .map(auth -> AuthCategoryEntity.initAuthCategoryEntity(auth))
                        .collect(Collectors.toList()));
        authGrantAccessCheckContext.setUsersInfo(
                GrantUserLogin.createGrantUserLogin(authorityApplyRequest));

    }

    // if the request of auths contains auth that dont exist in the range will failed and throw ex
    public void preCheckExpectAuthsValidation(List<AuthCategoryEntity> expectAuths) {
        boolean containsInvalid = authService.isAuthsExistsInvalid(expectAuths);
        if (containsInvalid) {
            throw new AuthException(AuthDesc.INVALID_AUTH_APPLY, null);
        }
    }

    private void checkUsersStatusAndRole(GrantUserLogin grantUserLogin) {
        // if the user is not admin will throw exception in service method
        UserDTO adminUser = userService.checkUserRole(UserLogin.buildUserLogin(
                grantUserLogin, Boolean.TRUE), true);
        //same as previous if check fail will throw exception
        UserDTO grantUser = userService.checkUserRole(UserLogin.buildUserLogin(
                grantUserLogin, Boolean.FALSE), false);

        //map UsersInfo to context for update or insert to auth_table
        grantUserLogin.setUserName(grantUser.getAccountName());
        grantUserLogin.setAdminUserName(adminUser.getAccountName());

        boolean isAdminUserActive = userService.checkUserStatus(adminUser.getStatus());
        boolean isGrantUserActive = userService.checkUserStatus(grantUser.getStatus());

        // either of admin and user exist one status not active will refuse grant process
        if (!isAdminUserActive || !isGrantUserActive) {
            String userId = isAdminUserActive ? grantUser.getUserId() : adminUser.getUserId();
            log.error("User status is not active, userId: " + userId);
            throw new AuthException(AuthDesc.USER_STATUS_NOT_ACTIVE, userId);
        }
    }

    private void checkUserExistsAuthStatus(List<AuthCategoryEntity> expectAuths, GrantUserLogin grantUserLogin) {

        // this already been set in buildcontext
        Objects.requireNonNull(expectAuths);

        // get tobeGrantedUser currently hold auths
        // cause we pass userId validation check here userId is valid
        List<AuthDTO> userAuths = userService.getUserAuthsByUserId(grantUserLogin.getUserId());

        updateExceptAuthsWithDbResult(userAuths, expectAuths);

    }

    private void updateExceptAuthsWithDbResult(List<AuthDTO> userAuths,
                                               List<AuthCategoryEntity> authCategoryEntities) {
        if (CollectionUtils.isEmpty(userAuths)) {
            return;
        }

        // to map dbResult back to context
        for (AuthCategoryEntity expectAuth : authCategoryEntities) {
            for (AuthDTO auth : userAuths) {
                if (StringUtils.equals(auth.getAuthCategory(), expectAuth.getAuthName())) {
                    expectAuth.setUserHeld(Boolean.TRUE);
                    expectAuth.setAuthStatus(auth.getStatus());
                    expectAuth.setGrantTime(auth.getCreateTime());
                }
            }
        }
    }

    // will set List in context of possibleGrantAuths
    private void dispatchAuthsAndUpdate(AuthGrantAccessCheckContext authGrantAccessCheckContext) {

        // this has been set in previous step
        List<AuthCategoryEntity> expectGrantAuths = authGrantAccessCheckContext.getExpectedGrantAuths();
        List<AuthCategoryEntity> possibleGrantAuths = authService.getPossibleGrantAuths(expectGrantAuths);
        List<AuthCategoryEntity> userHoldActiveAtuhs = authService.getUserHoldActiveAuths(expectGrantAuths);

        if (CollectionUtils.isEmpty(possibleGrantAuths))
            return;
        // for build resp if success save
        authGrantAccessCheckContext.setPossibleGrantAuths(possibleGrantAuths);
        authGrantAccessCheckContext.setUserHoldActiveAuths(userHoldActiveAtuhs);
        authService.savePossibleAuthsDataInDb(possibleGrantAuths, authGrantAccessCheckContext);
    }

    private  AuthGrantResp createGrantAuthSucessResp(GrantUserLogin grantUserLogin,
                                                          AuthGrantAccessCheckContext accessCheckContext){
        AuthGrantResp authGrantResp= AuthGrantResp.builder()
                .grantTime(accessCheckContext.getAccessTime())
                .userId(grantUserLogin.getUserId())
                .userName(grantUserLogin.getUserName())
                .grantUserId(grantUserLogin.getAdminUserId())
                .grantUserName(grantUserLogin.getAdminUserName())
                .successGrants(accessCheckContext.getPossibleGrantAuths())
                .userHoldActive(accessCheckContext.getUserHoldActiveAuths())
                .successCount(accessCheckContext.getPossibleGrantAuths().size())
                .build();
        if(authGrantResp.getSuccessCount()!=0)
            authGrantResp.setGrantTime(accessCheckContext.getAccessTime());
        authGrantResp.setResultCode(AuthDesc.SUCCESS.getResCode());
        authGrantResp.setResultDescription(AuthDesc.SUCCESS.getResDesc());
        return authGrantResp;
    }
}
