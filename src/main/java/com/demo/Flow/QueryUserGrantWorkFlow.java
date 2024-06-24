package com.demo.Flow;

import com.demo.constant.AuthDesc;
import com.demo.constant.AuthStatus;
import com.demo.context.QueryContext;
import com.demo.entity.AuthCategoryEntity;
import com.demo.entity.DTO.AuthDTO;
import com.demo.entity.UserLogin;
import com.demo.entity.resp.QueryResp;
import com.demo.service.AuthService;
import com.demo.util.AuthCategoryUtils;
import com.demo.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Component
public class QueryUserGrantWorkFlow {

    @Autowired
    AuthService authService;


    public QueryResp doProcess(String userId,String authName){

        QueryContext queryContext=new QueryContext();

        buildQueryContext(userId,authName,queryContext);

        QueryResp queryResp=preCheckUserAndAuth(queryContext);
        if(!ObjectUtils.isEmpty(queryResp))
            return queryResp;

        return QueryResp.buildSuccessQueryResp(queryContext);
    }



    private void buildQueryContext(String userId,String authName,QueryContext queryContext){
        queryContext.setUserId(userId);
        queryContext.setAuthName(authName);
        queryContext.setUserLogin(UserLogin.buildUserLogin(userId));
        queryContext.setAccessTime(DateUtils.getCurrentDate());
    }

    private QueryResp preCheckUserAndAuth(QueryContext queryContext){
        if(!AuthCategoryUtils.isValidAuth(queryContext.getAuthName()))
            return QueryResp.createNotExistsAuthResp();
        // according to the pdf this interface has passed previous check
        // do need check user Anymore
        AuthDTO authDTO = authService.queryUserAuthsByIdName(queryContext);

        // dereg-not exists   frobidden-show to user
        if(Objects.isNull(authDTO)||Objects.equals(authDTO.getStatus(), AuthStatus.DEREG))
            return QueryResp.createNotExistsAuthResp();
        AuthCategoryEntity authEntity = AuthCategoryEntity.initAuthCategoryEntity(queryContext.getAuthName());
        AuthCategoryEntity.mapAuthDtoBackToEntity(authEntity,authDTO);
        queryContext.setAuthCategoryEntity(authEntity);
       return null;
    }
}
