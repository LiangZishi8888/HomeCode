package com.demo.dao;

import com.demo.entity.DTO.AuthDTO;
import com.demo.entity.DTO.UserDTO;

import java.util.Date;
import java.util.List;

public interface UserDao {

    UserDTO findUserById(String userId);

    int updateLoginTimeById(String userId, Date loginTime);

}
