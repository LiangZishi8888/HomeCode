package com.service;

import com.constant.UserStatus;

import com.entity.DTO.AuthInDb;
import com.entity.DTO.User;
import com.entity.UserLogin;

import java.util.List;

public interface UserService {

    User checkUserRole(UserLogin userLogin,boolean adminOnly);

    boolean isLoginUserAdmin(UserLogin user);

    void updateUserLoginTime(UserLogin userLogin);

    Boolean checkUserStatus(UserStatus userStatus);

    void checkUserSignature(String userSignature,String userId);

    List<AuthInDb> getUserAuths(String userId);

}
