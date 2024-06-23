package com.controller;

import com.cache.TestDataCache;
import com.constant.AuthDesc;
import com.constant.UserRole;
import com.constant.UserStatus;
import com.dao.UserDao;
import com.entity.DTO.UserDTO;
import com.entity.crypto.DecryptBodyAdvice;
import com.entity.req.AuthorityApplyRequest;
import com.entity.req.UserLoginRequest;
import com.entity.resp.AuthGrantResp;
import com.entity.resp.UserLoginAccessCheckResp;
import com.handler.GlobalExceptionHandler;
import com.util.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.utils.MockUtils.postPerform;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class AuthorityControllerTest extends AbstractJUnit4SpringContextTests {

    private static final String ACCESS_CHECK_URL = "/role/access";

    private static final String GRANT_AUTHS_URL="/admin/addUser";

    private MockMvc mockMvc;

    @Autowired
    private AuthorityController authorityController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private DecryptBodyAdvice decryptBodyAdvice;

    @Autowired
    private UserDao userDao;

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                authorityController, globalExceptionHandler, decryptBodyAdvice).build();
    }

    @Test
    public void should_return_success_when_user_exists_and_active() {

        //before-login
        UserDTO adminUser = userDao.findUserById("123456");
        Assert.assertTrue(adminUser.getStatus().getStatus().equals(UserStatus.ACTIVE.getStatus()));

        UserLoginRequest req = TestDataCache.getMockReq("appCreate1",
                UserLoginRequest.class);
        UserLoginAccessCheckResp resp = postPerform(mockMvc, req,
                UserLoginAccessCheckResp.class, ACCESS_CHECK_URL, true);
        Assert.assertEquals(AuthDesc.SUCCESS.getResCode(), resp.getResultCode());
        Assert.assertEquals(AuthDesc.SUCCESS.getResDesc(), resp.getResultDescription());
        Assert.assertEquals(adminUser.getLastLoginTime(), resp.getLastLoginTime());

        UserDTO adminUserAfterLogin = userDao.findUserById("123456");
        //after-login,last_login_time update with current access loginTime
        Assert.assertEquals(adminUserAfterLogin.getLastLoginTime(), resp.getLoginTime());
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();
    }

    @Test
    public void should_throw_ex_when_user_not_exist() {
        UserLoginRequest req = TestDataCache.getMockReq("appCreate4",
                UserLoginRequest.class);
        UserLoginAccessCheckResp resp =
                postPerform(mockMvc, req, UserLoginAccessCheckResp.class, ACCESS_CHECK_URL, true);
        Assert.assertEquals(AuthDesc.USER_NOT_EXIST.getResCode(), resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_NOT_EXIST.getResDesc(), resp.getResultDescription());
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();
    }

    @Test
    public void should_throw_ex_when_user_try_to_access_with_admin_role() {
        UserLoginRequest req = TestDataCache.getMockReq("appCreate2",
                UserLoginRequest.class);

        // appCreate2 user is role of user we set it admin
        Assert.assertEquals(req.getRole(), UserRole.USER.getRole());
        req.setRole("admin");
        UserLoginAccessCheckResp resp =
                postPerform(mockMvc, req, UserLoginAccessCheckResp.class, ACCESS_CHECK_URL, true);
        Assert.assertEquals(AuthDesc.USER_NEED_ADMIN_PERMISSION.getResCode(), resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_NEED_ADMIN_PERMISSION.getResDesc(), resp.getResultDescription());
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();
    }

    @Test
    public void should_throw_ex_user_not_exist_when_user_dereg() {
        UserLoginRequest req = TestDataCache.getMockReq("appCreate3",
                UserLoginRequest.class);
        Assert.assertEquals(UserRole.USER.getRole(), req.getRole());
        UserLoginAccessCheckResp resp =
                postPerform(mockMvc, req, UserLoginAccessCheckResp.class, ACCESS_CHECK_URL, true);
        Assert.assertEquals(AuthDesc.USER_NOT_EXIST.getResCode(), resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_NOT_EXIST.getResDesc(), resp.getResultDescription());
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();
    }

    @Test
    public void should_return_correct_resp_when_user_frozen() {
        //before-login
        UserDTO frozenUser = userDao.findUserById("123405");
        Assert.assertTrue(frozenUser.getStatus().getStatus().equals(UserStatus.FROZEN.getStatus()));

        UserLoginRequest req = TestDataCache.getMockReq("appCreate5",
                UserLoginRequest.class);
        UserLoginAccessCheckResp resp = postPerform(mockMvc, req,
                UserLoginAccessCheckResp.class, ACCESS_CHECK_URL, true);
        Assert.assertEquals(AuthDesc.USER_STATUS_NOT_ACTIVE.getResCode(), resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_STATUS_NOT_ACTIVE.getResDesc(), resp.getResultDescription());
        Assert.assertEquals(frozenUser.getLastLoginTime(), resp.getLastLoginTime());

        UserDTO frozenUserAferAccess = userDao.findUserById("123405");
        //after-access no data modified in db
        Assert.assertEquals(frozenUserAferAccess.getLastLoginTime(), frozenUser.getLastLoginTime());
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();
    }

    @Test
    public void should_throw_ex_when_userid_not_compatible_with_userName() {
        UserLoginRequest req = TestDataCache.getMockReq("appCreate2",
                UserLoginRequest.class);

        // modified its name
        req.setAccountName("modified");
        UserLoginAccessCheckResp resp =
                postPerform(mockMvc, req, UserLoginAccessCheckResp.class, ACCESS_CHECK_URL, true);
        Assert.assertEquals(AuthDesc.USER_NOT_EXIST.getResCode(), resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_NOT_EXIST.getResDesc(), resp.getResultDescription());
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();
    }

    /**
     * belows are unit test for grant interface
     * we dont build unit test for login repeat
     */

    @Test
    public void should_throw_ex_when_user_try_to_grant_auths_with_admin_role() {
        AuthorityApplyRequest req = TestDataCache.getMockReq("grantApply1",
                AuthorityApplyRequest.class);
        UserDTO userById = userDao.findUserById(req.getUserId());
        Assert.assertNotEquals(userById.getRole(),UserRole.ADMIN.getRole());
        AuthGrantResp resp =
                postPerform(mockMvc, req, AuthGrantResp.class, GRANT_AUTHS_URL, false);
        Assert.assertEquals(AuthDesc.USER_NEED_ADMIN_PERMISSION.getResCode(), resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_NEED_ADMIN_PERMISSION.getResDesc(), resp.getResultDescription());
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();
    }

    @Test
    public void should_throw_ex_when_user_try_to_apply_auths_not_exists() {
        AuthorityApplyRequest req = TestDataCache.getMockReq("grantApply2",
                AuthorityApplyRequest.class);
        AuthGrantResp resp =
                postPerform(mockMvc, req, AuthGrantResp.class, GRANT_AUTHS_URL, false);
        Assert.assertEquals(AuthDesc.INVALID_AUTH_APPLY.getResCode(), resp.getResultCode());
        Assert.assertEquals(AuthDesc.INVALID_AUTH_APPLY.getResDesc(), resp.getResultDescription());
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();
    }

    @Test
    public void should_throw_ex_when_user_status_not_active() {
        AuthorityApplyRequest req = TestDataCache.getMockReq("grantApply3",
                AuthorityApplyRequest.class);
        UserDTO userById = userDao.findUserById(req.getUserId());
        Assert.assertNotEquals(userById.getStatus().getStatus(),UserStatus.ACTIVE.getStatus());

        AuthGrantResp resp =
                postPerform(mockMvc, req, AuthGrantResp.class, GRANT_AUTHS_URL, false);
        Assert.assertEquals(AuthDesc.USER_STATUS_NOT_ACTIVE.getResCode(), resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_STATUS_NOT_ACTIVE.getResDesc(), resp.getResultDescription());
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();
    }
}
