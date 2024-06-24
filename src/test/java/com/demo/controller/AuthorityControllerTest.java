package com.demo.controller;

import com.cache.TestDataCache;
import com.demo.constant.*;
import com.demo.dao.AuthDao;
import com.demo.dao.UserDao;
import com.demo.entity.AuthCategoryEntity;
import com.demo.entity.DTO.AuthDTO;
import com.demo.entity.DTO.UserDTO;
import com.demo.entity.crypto.DecryptBodyAdvice;
import com.demo.entity.req.AuthorityApplyRequest;
import com.demo.entity.req.UserLoginRequest;
import com.demo.entity.resp.AuthGrantResp;
import com.demo.entity.resp.QueryResp;
import com.demo.entity.resp.UserLoginAccessCheckResp;
import com.demo.handler.GlobalExceptionHandler;
import com.demo.util.JsonUtil;
import org.h2.util.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.utils.MockUtils.postPerform;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class AuthorityControllerTest {

    private static final String ACCESS_CHECK_URL = "/role/access";

    private static final String GRANT_AUTHS_URL="/admin/addUser";

    private static final String QUERY_AUTH_URL="/query/user";

    private MockMvc mockMvc;

    @Autowired
    private AuthorityController authorityController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private DecryptBodyAdvice decryptBodyAdvice;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthDao authDao;

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

    /**
     * dick previous two auths authA is active and authB is forbidden grant by nelson
     * authA no need update
     * authB need update and grant time as the current,change to current admin,change status active
     * associationNo no need update cause these two auths are exist by user
     * run this test individualy
     */
    @Test
    public void should_return_success_when_user_apply_grants_all_valid() {
        AuthorityApplyRequest req = TestDataCache.getMockReq("grantApply4",
                AuthorityApplyRequest.class);
        //query all user exists results
        List<AuthDTO> userAlreadyHolds = authDao.getUserAuthsById(req.getUserId());

        //dispatch in this case each only have one
        AuthDTO autha=userAlreadyHolds.stream().filter(dto-> StringUtils.equals(dto.getAuthCategory(), AuthCategory.AUTHA.getName())).
                collect(Collectors.toList()).get(0);
        AuthDTO authb=userAlreadyHolds.stream().filter(dto-> StringUtils.equals(dto.getAuthCategory(), AuthCategory.AUTHB.getName())).
                collect(Collectors.toList()).get(0);

        AuthGrantResp resp =
                postPerform(mockMvc, req, AuthGrantResp.class, GRANT_AUTHS_URL, false);
       Assert.assertEquals(AuthDesc.SUCCESS.getResDesc(),resp.getResultDescription());
       Assert.assertEquals(AuthDesc.SUCCESS.getResCode(),resp.getResultCode());
       Assert.assertEquals(req.getUserId(),resp.getUserId());
       Assert.assertEquals(req.getAdminUserId(),resp.getAdminUserId());

       // authB  authb is query before sending request
        AuthCategoryEntity successGrant = resp.getSuccessGrants().get(0);
        Assert.assertEquals(successGrant.getAuthName(),authb.getAuthCategory());
        Assert.assertEquals(successGrant.getAssociationNo(),authb.getAuthAssociationId());

        // adminUserName grantTime status should updated
        Assert.assertNotEquals(successGrant.getAuthStatus(),authb.getStatus());
        Assert.assertNotEquals(successGrant.getPreviousAdminUserName(),resp.getAdminUserName());
        Assert.assertNotEquals(successGrant.getGrantTime(),successGrant.getPreviousGrantDate());
        Assert.assertNotEquals(successGrant.getGrantTime(),authb.getCreateTime());

        Assert.assertEquals(successGrant.getPreviousAdminUserId(),authb.getAdminUserId());
        Assert.assertEquals(successGrant.getPreviousGrantDate(),authb.getCreateTime());


        // authA autha is query before sending request previous active no modification
        AuthCategoryEntity previousHoldActive = resp.getUserPreviousHoldActive().get(0);
        Assert.assertEquals(previousHoldActive.getAuthName(),autha.getAuthCategory());
        Assert.assertEquals(previousHoldActive.getAssociationNo(),autha.getAuthAssociationId());
        Assert.assertEquals(previousHoldActive.getAuthStatus(),autha.getStatus());
        Assert.assertEquals(previousHoldActive.getPreviousAdminUserName(),autha.getAdminUserName());
        Assert.assertEquals(previousHoldActive.getGrantTime(),successGrant.getPreviousGrantDate());
        Assert.assertEquals(previousHoldActive.getPreviousAdminUserId(),autha.getAdminUserId());
        Assert.assertEquals(previousHoldActive.getPreviousGrantDate(),autha.getCreateTime());
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();
    }

    /**
     * mark has no auths
     * thus he grant A,B,C should all insert into db
     * and return success
     */
    @Test
    public void should_add_all_success(){
        AuthorityApplyRequest req = TestDataCache.getMockReq("grantApply5",
                AuthorityApplyRequest.class);
        List<AuthDTO> userAuths = authDao.getUserAuthsById(req.getUserId());
        Assert.assertTrue(CollectionUtils.isEmpty(userAuths));


        AuthGrantResp resp =
                postPerform(mockMvc, req, AuthGrantResp.class, GRANT_AUTHS_URL, false);
        Assert.assertTrue(resp.getSuccessCount()==3);
        List<AuthDTO> afterGrant = authDao.getUserAuthsById(req.getUserId());
        for(AuthDTO authDTO:afterGrant){
            checkInsert(authDTO,resp);
        }
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();
    }

    private void checkInsert(AuthDTO authDTO,AuthGrantResp authGrantResp){
        Assert.assertEquals(authDTO.getAdminUserId(),authGrantResp.getAdminUserId());
        Assert.assertEquals(authDTO.getAdminUserName(),authGrantResp.getAdminUserName());
        Assert.assertEquals(authDTO.getCreateTime(),authGrantResp.getGrantTime());
        Assert.assertEquals(authDTO.getStatus(), AuthStatus.ACTIVE);
    }

    /**
     * userId of 123401 hold authB
     */
    @Test
    public void should_return_success_when_auth_exists(){
        QueryResp queryResp=getPerform(mockMvc,QueryResp.class,QUERY_AUTH_URL,"123401","authB");
        Assert.assertEquals(queryResp.getIsExists(),Boolean.TRUE);
        System.out.println(JsonUtil.objToJson(queryResp));
        System.out.println();
    }

    /**
     * user 123402 dont have authA
     */
    @Test
    public void should_return_false_when_auth_not_exists(){
        QueryResp queryResp=getPerform(mockMvc,QueryResp.class,QUERY_AUTH_URL,"123402","authA");
        Assert.assertEquals(queryResp.getIsExists(),Boolean.FALSE);
        System.out.println(JsonUtil.objToJson(queryResp));
        System.out.println();
    }

    /**
     * authD is not defined in the system
     */
    @Test
    public void should_return_false_when_auth_is_not_accept_by_system(){
        QueryResp queryResp=getPerform(mockMvc,QueryResp.class,QUERY_AUTH_URL,"123402","authD");
        Assert.assertEquals(queryResp.getIsExists(),Boolean.FALSE);
        System.out.println(JsonUtil.objToJson(queryResp));
        System.out.println();
    }

    /**
     * peter 123406 has two auths
     *    authA grant By Eric status active
     *    authB grant By  Nelson status forbidden
     *
     *    now he wants to apply A.B.C
     *    and query A,B
     *    grant is by  eric
     *
     *    A no modify
     *    B is update record shows grant by Eric now
     *    C is insert
     */
    @Test
    @Transactional
    public void  Union_together_Test(){
        UserLoginRequest req = TestDataCache.getMockReq("unionApply1",
                UserLoginRequest.class);
        UserLoginAccessCheckResp resp =
                postPerform(mockMvc, req, UserLoginAccessCheckResp.class, ACCESS_CHECK_URL, true);
        Assert.assertEquals(resp.getResultCode(),AuthDesc.SUCCESS.getResCode());
        // Peter is active
        Assert.assertEquals(resp.getUserStatus(),UserStatus.ACTIVE.getStatus());
        //Active user resp last_login_time should not equal to login_time
        Assert.assertNotEquals(resp.getLastLoginTime(),resp.getLoginTime());
        System.out.println(JsonUtil.objToJson(resp));
        System.out.println();

        AuthorityApplyRequest grantReq = TestDataCache.getMockReq("unionApply2",
                AuthorityApplyRequest.class);
        AuthGrantResp grantResp =
                postPerform(mockMvc,grantReq, AuthGrantResp.class, GRANT_AUTHS_URL, false);
        //BC was modified  success=2
        Assert.assertEquals(grantResp.getSuccessCount(),Integer.valueOf(2));
        System.out.println(JsonUtil.objToJson(grantResp));
        System.out.println();

        //Peter has an active auth grant by Eric previously resp has only one according to the story
        Date grantAccessTime = grantResp.getGrantTime();
        AuthCategoryEntity authA = grantResp.getUserPreviousHoldActive().get(0);
        Assert.assertEquals(authA.getPreviousAdminUserId(),grantResp.getAdminUserId());

        // authB previous grant is Nelson
        AuthCategoryEntity authB = getAuthFromSuceesGrants(grantResp.getSuccessGrants(), "authB");
        Assert.assertNotEquals(authB.getPreviousAdminUserId(),grantResp.getAdminUserId());
        Assert.assertNotEquals(authB.getPreviousGrantDate(),grantAccessTime);

        QueryResp queryResp1=getPerform(mockMvc,QueryResp.class,QUERY_AUTH_URL,req.getUserId(),"authA");
        Assert.assertEquals(queryResp1.getIsExists(),Boolean.TRUE);
        System.out.println(JsonUtil.objToJson(queryResp1));
        System.out.println();

        QueryResp queryResp2=getPerform(mockMvc,QueryResp.class,QUERY_AUTH_URL,req.getUserId(),"authB");
        Assert.assertEquals(queryResp2.getIsExists(),Boolean.TRUE);
        System.out.println(JsonUtil.objToJson(queryResp2));
        System.out.println();


        QueryResp queryResp3=getPerform(mockMvc,QueryResp.class,QUERY_AUTH_URL,req.getUserId(),"authC");
        Assert.assertEquals(queryResp3.getIsExists(),Boolean.TRUE);
        System.out.println(JsonUtil.objToJson(queryResp3));
        System.out.println();

        //B is forbidden and now should be active
        Assert.assertEquals(queryResp2.getAuthDetail().getAuthStatus(),AuthStatus.ACTIVE);


    }

    private static MockHttpServletRequestBuilder buildHttpGetReq(String url, String... params){
        return MockMvcRequestBuilders.get(url)
                .param("userId",params[0])
                .param("authName",params[1]);
    }

    public static <R> R getPerform(MockMvc mockMvc,Class<R> respClass,String url,String...params){
        try {
            MvcResult mvcResult = mockMvc.perform(buildHttpGetReq(url, params))
                    .andReturn();
            R r = JsonUtil.JsonToObj(mvcResult.getResponse().getContentAsString(), respClass);
            return r;
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
        return null;
    }

    /**
     * for test
     */
    private AuthCategoryEntity getAuthFromSuceesGrants(List<AuthCategoryEntity> authCategoryEntities,String authName){
        for(AuthCategoryEntity authCategoryEntity:authCategoryEntities){
            if(authCategoryEntity.getAuthName().equals(authName))
                return authCategoryEntity;
        }
        return null;
    }
}
