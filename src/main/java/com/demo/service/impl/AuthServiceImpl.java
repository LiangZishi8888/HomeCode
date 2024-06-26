package com.demo.service.impl;

import com.demo.constant.AuthStatus;
import com.demo.context.AuthGrantAccessCheckContext;
import com.demo.context.QueryContext;
import com.demo.dao.AuthDao;
import com.demo.entity.AuthCategoryEntity;
import com.demo.entity.DTO.AuthDTO;
import com.demo.entity.GrantUserLogin;
import com.demo.generator.impl.AuthAssoNoGenerator;
import com.demo.service.AuthService;
import com.demo.service.AuthorityDBService;
import com.demo.util.AuthCategoryUtils;
import com.demo.util.DRDSDbSplitKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthDao authDao;

    @Autowired
    AuthorityDBService authorityDBService;

    @Override
    public boolean isAuthsExistsInvalid(List<AuthCategoryEntity> authCategoryEntities) {
        Objects.requireNonNull(authCategoryEntities);
        boolean containsInvalid = false;
        for (AuthCategoryEntity auth : authCategoryEntities) {
            if (!AuthCategoryUtils.isValidAuth(auth.getAuthName())) {
                containsInvalid = true;
                // we dont expose authName or we can expose for security depending
                // if dont expose just record log
                log.error("invalid auth application ,authName: ", auth.getAuthName());
            }
        }
        return containsInvalid;
    }

    @Override
    public List<AuthCategoryEntity> getPossibleGrantAuths(List<AuthCategoryEntity> authCategoryEntities) {
        return authCategoryEntities.stream()
                        .filter(auth->!AuthCategoryEntity.isUserHoldActiveAuth(auth))
                                .collect(Collectors.toList());
    }

    @Override
    public List<AuthCategoryEntity> getUserHoldActiveAuths(List<AuthCategoryEntity> authCategoryEntities) {
        return authCategoryEntities.stream()
                // user not hold or user hold while status !=active will be filtered
                .filter(auth->AuthCategoryEntity.isUserHoldActiveAuth(auth))
                .collect(Collectors.toList());
    }

    @Override
    public void savePossibleAuthsDataInDb(List<AuthCategoryEntity> possibleAuths,
                                          AuthGrantAccessCheckContext authGrantContext) {
        List<AuthDTO> auths = possibleAuths.stream()
                .map(entity ->{
                    // just modifiy possible auths in expectGrants
                    // user hold will not effect
                    entity.setAuthStatus(AuthStatus.ACTIVE);
                    entity.setGrantTime(authGrantContext.getAccessTime());
                    AuthDTO authDTO=initAuthDTO(entity, authGrantContext);
                    return authDTO;
                })
                .collect(Collectors.toList());
        authorityDBService.saveAuthsInDb(auths);
    }

    @Override
    public AuthDTO queryUserAuthsByIdName(QueryContext queryContext) {
        return authDao.getUserAuthByIdAuthName(queryContext.getUserId(),queryContext.getAuthName());
    }

    private AuthDTO initAuthDTO(AuthCategoryEntity authCategoryEntity,
                                AuthGrantAccessCheckContext authGrantContext) {
        GrantUserLogin grantUserLogin = authGrantContext.getUsersInfo();
        AuthDTO auth = AuthDTO.builder()
                .userId(grantUserLogin.getUserId())
                .userName(grantUserLogin.getUserName())
                .adminUserId(grantUserLogin.getAdminUserId())
                .adminUserName(grantUserLogin.getAdminUserName())
                .status(AuthStatus.ACTIVE)
                .authCategory(authCategoryEntity.getAuthName())
                // makeSure  a users all auths in the same database
                .dbSplitKey(DRDSDbSplitKeyUtils.calculateDbSplitKey(grantUserLogin.getUserId()))
                .build();

        // userHold will not set and make a mark to determin whether insert or update
        if(!authCategoryEntity.getIsUserHeld()) {
            auth.setAuthAssociationId(AuthAssoNoGenerator.generateAssoNo());
            auth.setCreateTime(authGrantContext.getAccessTime());
            authCategoryEntity.setAssociationNo(auth.getAuthAssociationId());
        }else{
            auth.setLastModifyTime(authGrantContext.getAccessTime());
        }
        return auth;
    }
}
