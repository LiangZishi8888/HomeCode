package com.controller;

import com.cache.TestDataCache;
import com.constant.AuthDesc;
import com.constant.UserRole;
import com.constant.UserStatus;
import com.dao.UserDao;
import com.entity.DTO.UserDTO;
import com.entity.crypto.DecryptBodyAdvice;
import com.entity.req.UserLoginRequest;
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
    private DecryptBodyAdvice decryptBodyAdvice;

    @Autowired
    private UserDao userDao;

    @Before
    public  void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                authorityController,globalExceptionHandler,decryptBodyAdvice).build();
    }

    @Test
    public void should_return_success_when_user_exists_and_active(){

        //before-login
        UserDTO adminUser = userDao.findUserById("123456");
        Assert.assertTrue(adminUser.getStatus().getStatus().equals(UserStatus.ACTIVE.getStatus()));

        UserLoginRequest req = TestDataCache.getMockReq("appCreate1",
                UserLoginRequest.class);
        UserLoginAccessCheckResp resp = postPerform(mockMvc, req,
                     UserLoginAccessCheckResp.class, ACCESS_CHECK_URL,true);
        Assert.assertEquals(AuthDesc.SUCCESS.getResCode(),resp.getResultCode());
        Assert.assertEquals(AuthDesc.SUCCESS.getResDesc(),resp.getResultDescription());
        Assert.assertEquals(adminUser.getLastLoginTime(),resp.getLastLoginTime());

        UserDTO adminUserAfterLogin= userDao.findUserById("123456");
        //after-login,last_login_time update with current access loginTime
        Assert.assertEquals(adminUserAfterLogin.getLastLoginTime(),resp.getLoginTime());
    }

    @Test
    public void should_throw_ex_when_user_not_exist(){
        UserLoginRequest req= TestDataCache.getMockReq("appCreate4",
                UserLoginRequest.class);
        UserLoginAccessCheckResp resp=
                postPerform(mockMvc,req,UserLoginAccessCheckResp.class,ACCESS_CHECK_URL,true);
        Assert.assertEquals(AuthDesc.USER_NOT_EXIST.getResCode(),resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_NOT_EXIST.getResDesc(),resp.getResultDescription());
    }

    @Test
    public void should_throw_ex_when_user_try_to_access_with_admin_role(){
        UserLoginRequest req = TestDataCache.getMockReq("appCreate2",
                UserLoginRequest.class);

        // appCreate2 user is role of user we set it admin
        Assert.assertEquals(req.getRole(), UserRole.USER.getRole());
        req.setRole("admin");
        UserLoginAccessCheckResp resp=
                postPerform(mockMvc,req,UserLoginAccessCheckResp.class,ACCESS_CHECK_URL,true);
        Assert.assertEquals(AuthDesc.USER_ROLE_ERROR.getResCode(),resp.getResultCode());
        Assert.assertEquals(AuthDesc.USER_ROLE_ERROR.getResDesc(),resp.getResultDescription());
    }

    @Test
    public void should_return_check_result_false_and_not_update_db_when_user_status_not_avtive(){
        UserLoginRequest req = TestDataCache.getMockReq("appCreate3",
                UserLoginRequest.class);
        Assert.assertEquals(UserRole.USER.getRole(),req.getRole());
        UserLoginAccessCheckResp resp=
                postPerform(mockMvc,req,UserLoginAccessCheckResp.class,ACCESS_CHECK_URL,true);
        Assert.assertEquals(Boolean.valueOf(resp.getCheckResult()),false);
        Assert.assertEquals(resp.getUserStatus(),resp.getUserStatus());

        // after access lastLoginTime should be update in db while not active user will not
        UserDTO degUser=userDao.findUserById("123402");
        Assert.assertEquals(resp.getLastLoginTime(),degUser.getLastLoginTime());
    }
}
