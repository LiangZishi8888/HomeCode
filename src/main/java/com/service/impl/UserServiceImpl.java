package com.service.impl;

import com.constant.AuthDesc;
import com.constant.AuthException;
import com.constant.UserRole;
import com.constant.UserStatus;
import com.dao.UserDao;
import com.entity.DTO.AuthInDb;
import com.entity.DTO.User;
import com.entity.UserLogin;
import com.entity.crypto.CipherUtils;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    // here we design another paramter adminOnly for grant authority situation
    // in grant resource situation only adminOnly user can access this system
    // so that we can reuse this part of code
    @Override
    public User checkUserRole(UserLogin userLogin, boolean adminOnly) {
        User userInDb = userDao.findUserById(userLogin.getUserId());
        if (Objects.isNull(userInDb)) {
            log.error("user not exists,userId : {}", userLogin.getUserId());
            throw new AuthException(AuthDesc.USER_NOT_EXIST,userLogin.getUserId());
        }

        boolean isUserInDbAdmin = StringUtils.equals(userInDb.getRole(), UserRole.ADMIN.getRole());
        boolean requiredAdmin=adminOnly||isLoginUserAdmin(userLogin);

        // user is not admin while try to access with admin role
        if (!isUserInDbAdmin && requiredAdmin) {
            log.error("user has not grant admin authority,id: {}", userLogin.getUserId());
            throw new AuthException(AuthDesc.USER_NEED_ADMIN_PERMISSION,null);
        }

        // setName for only id access interface
        userLogin.setAccountName(userInDb.getAccountName());
        userLogin.setStatus(userInDb.getStatus());
        userLogin.setLastLoginTime(userInDb.getLastLoginTime());
        return userInDb;
    }

    @Override
    public boolean isLoginUserAdmin(UserLogin userLogin) {
        Objects.requireNonNull(userLogin);
        return StringUtils.equals(userLogin.getRole(), UserRole.ADMIN.getRole());
    }

    @Override
    public void updateUserLoginTime(UserLogin userLogin) {
        int successNum = userDao.updateLoginTimeById(userLogin.getUserId(),
                userLogin.getLoginTime());
        if (successNum != 1) {
            log.error("user not exists, userId:{}, accountName:{}",
                    userLogin.getUserId(), userLogin.getAccountName());
            throw new AuthException(AuthDesc.USER_NOT_EXIST,userLogin.getUserId());
        }
    }

    @Override
    public Boolean checkUserStatus(UserStatus userStatus) {
        Objects.requireNonNull(userStatus);
        switch (userStatus) {
            case ACTIVE:
                return Boolean.TRUE;
            case FROZEN:
                return Boolean.FALSE;
            case DEREG:
                return Boolean.FALSE;
            default:
                throw new AuthException(AuthDesc.UNOWN_USER_STATUS,userStatus.getStatus());
        }
    }

    @Override
    public List<AuthInDb> getUserAuths(String userId) {
        return null;
    }

}
