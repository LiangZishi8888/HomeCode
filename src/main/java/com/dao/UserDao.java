package com.dao;

import com.entity.DTO.User;

public interface UserDao {

    User findUserById(String userId,String accountName);

    int updateLoginTimeById(String userId,String loginTime);
}
