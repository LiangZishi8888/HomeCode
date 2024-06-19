package com.controller;

import com.cache.TestDataCache;
import com.constant.AuthDesc;
import com.constant.UserRole;
import com.constant.UserStatus;
import com.dao.UserDao;
import com.entity.DTO.User;
import com.entity.req.UserLoginRequestWithSign;
import com.entity.resp.UserLoginAccessCheckResp;
import com.handler.GlobalExceptionHandler;
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

/**
 * all the req in mockReqs file are signature valid with userId
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class AuthorityControllerTest extends AbstractJUnit4SpringContextTests {

    private static final String ACCESS_CHECK_URL="/role/access";

    private  MockMvc mockMvc;

    @Autowired
    private AuthorityController authorityController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private UserDao userDao;

    @Before
    public  void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authorityController,globalExceptionHandler).build();
    }

    @Test
    public void should_return_success_when_user_exists_and_active(){

        //before-login
        User adminUser = userDao.findUserById("123456", "Eric");
        Assert.assertTrue(adminUser.getStatus().getStatus().equals(UserStatus.ACTIVE.getStatus()));

        UserLoginRequestWithSign req = TestDataCache.getMockReq("appCreate1",
                UserLoginRequestWithSign.class);
        UserLoginAccessCheckResp resp =
             postPerform(mockMvc, req, UserLoginAccessCheckResp.class, ACCESS_CHECK_URL);
        Assert.assertEquals(AuthDesc.SUCCESS.getResCode(),resp.getResultCode());
        Assert.assertEquals(AuthDesc.SUCCESS.getResDesc(),resp.getResultDescription());
        Assert.assertEquals(adminUser.getLastLoginTime(),resp.getLastLoginTime());

        User adminUserAfterLogin= userDao.findUserById("123456", "Eric");
        //after-login,last_login_time update with current access loginTime
        Assert.assertEquals(adminUserAfterLogin.getLastLoginTime(),resp.getLoginTime());
    }

    @Test
    public void should_throw_ex_when_user_not_exist(){
        UserLoginRequestWithSign req = TestDataCache.getMockReq("appCreate4",
                UserLoginRequestWithSign.class);
        UserLoginAccessCheckResp resp=
                postPerform(mockMvc,req,UserLoginAccessCheckResp.class,ACCESS_CHECK_URL);
        Assert.assertEquals(AuthDesc.USER_NOT_EXIST.getResCode(),resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_NOT_EXIST.getResDesc(),resp.getResultDescription());
    }

    @Test
    public void should_throw_ex_when_user_try_to_access_with_admin_role(){
        UserLoginRequestWithSign req = TestDataCache.getMockReq("appCreate2",
                UserLoginRequestWithSign.class);

        // appCreate2 user is role of user we set it admin
        Assert.assertEquals(req.getRole(), UserRole.USER.getRole());
        req.setRole("admin");
        UserLoginAccessCheckResp resp=
                postPerform(mockMvc,req,UserLoginAccessCheckResp.class,ACCESS_CHECK_URL);
        Assert.assertEquals(AuthDesc.USER_ROLE_ERROR.getResCode(),resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_ROLE_ERROR.getResDesc(),resp.getResultDescription());
    }

    @Test
    public void should_return_check_result_false_and_not_update_db_when_user_status_not_avtive(){
        UserLoginRequestWithSign req = TestDataCache.getMockReq("appCreate3",
                UserLoginRequestWithSign.class);
        Assert.assertEquals(UserRole.USER.getRole(),req.getRole());
        UserLoginAccessCheckResp resp=
                postPerform(mockMvc,req,UserLoginAccessCheckResp.class,ACCESS_CHECK_URL);
        Assert.assertEquals(Boolean.valueOf(resp.getCheckResult()),false);
        Assert.assertEquals(resp.getUserStatus(),resp.getUserStatus());

        // after access lastLoginTime should be update in db while not active user will not
        User degUser=userDao.findUserById("123402","Lisa");
        Assert.assertEquals(resp.getLastLoginTime(),degUser.getLastLoginTime());
    }

    @Test
    public void should_throw_ex_when_modifiy_user_signature(){
        UserLoginRequestWithSign req = TestDataCache.getMockReq("appCreate1",
                UserLoginRequestWithSign.class);
        //  a user exists in db and modify signature
        String originalSign=req.getSignature();
        req.setSignature("f2661eea8d03db7e22f43a3e14a21548");
        Assert.assertNotEquals(originalSign,req.getSignature());
        UserLoginAccessCheckResp resp =
                postPerform(mockMvc, req, UserLoginAccessCheckResp.class, ACCESS_CHECK_URL);
        Assert.assertEquals(AuthDesc.USER_SIGNATURE_INVALID.getResCode(),resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_SIGNATURE_INVALID.getResDesc(),resp.getResultDescription());

    }

}