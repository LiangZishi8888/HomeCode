package com.demo.service;

import com.demo.constant.UserStatus;

import com.demo.entity.DTO.AuthDTO;
import com.demo.entity.DTO.UserDTO;
import com.demo.entity.UserLogin;

import java.util.List;

public interface UserService {

    UserDTO checkUserRole(UserLogin userLogin, boolean adminOnly);

    boolean isUserAdmin(UserDTO user);

    void updateUserLoginTime(UserLogin userLogin);

    Boolean checkUserStatus(UserStatus userStatus);

    List<AuthDTO> getUserAuthsByUserId(String userId);

}
