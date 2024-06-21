package com.service.impl;

import com.constant.AuthStatus;
import com.context.AuthGrantAccessCheckContext;
import com.dao.AuthDao;
import com.entity.AuthCategoryEntity;
import com.entity.DTO.AuthDTO;
import com.entity.GrantUserLogin;
import com.generator.impl.AuthAssoNoGenerator;
import com.service.AuthService;
import com.service.AuthorityDBService;
import com.util.AuthCategoryUtils;
import com.util.DRDSDbSplitKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.h2.util.StringUtils;
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
                .filter(authCategoryEntity ->
                        !authCategoryEntity.isUserHeld()
                                ||
                                //status in entity will set if authInDb match entity see previous precedure
                                StringUtils.equals(authCategoryEntity.getAuthStatus(), AuthStatus.DEREG.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public void savePossibleAuthsDataInDb(List<AuthCategoryEntity> authCategoryEntities,
                                             AuthGrantAccessCheckContext authGrantContext) {

        List<AuthDTO> auths = authCategoryEntities.stream()
                .map(entity -> initAuthDTO(entity, authGrantContext))
                .collect(Collectors.toList());
        authorityDBService.saveAuthsInDb(auths);
    }

    private AuthDTO initAuthDTO(AuthCategoryEntity authCategoryEntity,
                                AuthGrantAccessCheckContext authGrantContext) {
        GrantUserLogin grantUserLogin = authGrantContext.getUsersInfo();
        return AuthDTO.builder()
                .userId(grantUserLogin.getUserId())
                .userName(grantUserLogin.getUserName())
                .adminUserId(grantUserLogin.getAdminUserId())
                .adminUserName(grantUserLogin.getAdminUserName())
                .authAssociationId(AuthAssoNoGenerator.generateAssoNo())
                .createTime(authGrantContext.getAccessTime())
                .lastModifyTime(authGrantContext.getAccessTime())
                .status(authCategoryEntity.getAuthStatus())
                .authCategory(authCategoryEntity.getAuthName())
                // makeSure  a users all auths in the same database
                .dbSplitKey(DRDSDbSplitKeyUtils.calculateDbSplitKey(grantUserLogin.getUserId()))
                .build();
    }
}
