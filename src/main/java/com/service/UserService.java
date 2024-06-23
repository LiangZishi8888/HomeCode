package com.service;

import com.constant.UserStatus;

import com.entity.DTO.AuthDTO;
import com.entity.DTO.UserDTO;
import com.entity.UserLogin;

import java.util.List;

public interface UserService {

    UserDTO checkUserRole(UserLogin userLogin, boolean adminOnly);

    boolean isUserAdmin(UserDTO user);

    void updateUserLoginTime(UserLogin userLogin);

    Boolean checkUserStatus(UserStatus userStatus);

    List<AuthDTO> getUserAuthsByUserId(String userId);

}
