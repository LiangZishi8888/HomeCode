package com.demo.service;

import com.demo.context.AuthGrantAccessCheckContext;
import com.demo.context.QueryContext;
import com.demo.entity.AuthCategoryEntity;
import com.demo.entity.DTO.AuthDTO;

import java.util.List;

public interface AuthService {

    boolean isAuthsExistsInvalid(List<AuthCategoryEntity> authCategoryEntities) ;

    /**
     *   1.an authority can be grant,namely update or insert in db
     *       that auth user dont hold or user hold but status == detegisterd
     *   2.for auths which user held but status is FORBIDDEN need resume in other interface
     *   3. the grantApply interface will not process that apply casuse forbidden resume
     *       is  a passive action for user
     */
    List<AuthCategoryEntity> getPossibleGrantAuths(List<AuthCategoryEntity> authCategoryEntities);

    List<AuthCategoryEntity> getUserHoldActiveAuths(List<AuthCategoryEntity> authCategoryEntities);

    void savePossibleAuthsDataInDb(List<AuthCategoryEntity> authCategoryEntities,
                                   AuthGrantAccessCheckContext accessCheckContext);

    AuthDTO  queryUserAuthsByIdName(QueryContext queryContext);
}
