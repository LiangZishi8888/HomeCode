package com.service;

import com.constant.UserStatus;

import com.entity.DTO.User;
import com.entity.UserLogin;

public interface UserService {

    User checkUserRole(UserLogin userLogin,boolean adminOnly);

    boolean isLoginUserAdmin(UserLogin user);

    void updateUserLoginTime(UserLogin userLogin);

    Boolean checkUserStatus(UserStatus userStatus);

    void checkUserSignature(String userSignature,String userId);

}
