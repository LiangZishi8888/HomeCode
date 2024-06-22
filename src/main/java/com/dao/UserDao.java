package com.dao;

import com.entity.DTO.AuthDTO;
import com.entity.DTO.UserDTO;

import java.util.Date;
import java.util.List;

public interface UserDao {

    UserDTO findUserById(String userId);

    int updateLoginTimeById(String userId, Date loginTime);

    List<AuthDTO> queryUserAuths(String userId);
}
