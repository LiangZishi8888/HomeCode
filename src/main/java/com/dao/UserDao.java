package com.dao;

import com.entity.DTO.AuthInDb;
import com.entity.DTO.User;

import java.util.List;

public interface UserDao {

    User findUserById(String userId);

    int updateLoginTimeById(String userId,String loginTime);

    List<AuthInDb> queryUserAuths(String userId);
}
