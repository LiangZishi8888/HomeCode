package com.demo.service.impl;

import com.demo.constant.AuthDesc;
import com.demo.constant.AuthException;
import com.demo.constant.UserRole;
import com.demo.constant.UserStatus;
import com.demo.dao.AuthDao;
import com.demo.dao.UserDao;
import com.demo.entity.DTO.AuthDTO;
import com.demo.entity.DTO.UserDTO;
import com.demo.entity.UserLogin;
import com.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthDao authDao;

    // here we design another paramter adminOnly for grant authority situation
    // in grant resource situation only adminOnly user can access this system
    // so that we can reuse this part of code
    @Override
    public UserDTO checkUserRole(UserLogin userLogin, boolean adminOnly) {
        UserDTO userInDb = userDao.findUserById(userLogin.getUserId());
        // if user exist inDb but still status DEREG we expose the same result
        if (Objects.isNull(userInDb)||Objects.equals(userInDb.getStatus(),UserStatus.DEREG)) {
            log.error("user not exists,userId : {}", userLogin.getUserId());
            throw new AuthException(AuthDesc.USER_NOT_EXIST,userLogin.getUserId());
        }
        String userName=userLogin.getAccountName();

        // if we have login name check whether the name consists with dbRecord
        if(StringUtils.isNotEmpty(userName)&&!StringUtils.equals(userName,userInDb.getAccountName())){
            log.error("user login name is not compatible with db login:{},actual:{}",
                    userLogin.getAccountName(),userInDb.getAccountName());
            throw new AuthException(AuthDesc.USER_NOT_EXIST,userLogin.getUserId());
        }

        boolean isUserInDbAdmin = isUserAdmin(userInDb);
        // user is not admin while try to access with admin role
        if ( adminOnly&&!isUserInDbAdmin) {
            log.error("user has not grant admin authority,id: {}", userLogin.getUserId());
            throw new AuthException(AuthDesc.USER_NEED_ADMIN_PERMISSION,userInDb.getUserId());
        }

        // setName for only id access interface
        userLogin.setAccountName(userInDb.getAccountName());
        userLogin.setStatus(userInDb.getStatus());
        userLogin.setLastLoginTime(userInDb.getLastLoginTime());
        return userInDb;
    }

    @Override
    public boolean isUserAdmin(UserDTO userLogin) {
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
    public List<AuthDTO> getUserAuthsByUserId(String userId) {
        return authDao.getUserAuthsById(userId);
    }

}
