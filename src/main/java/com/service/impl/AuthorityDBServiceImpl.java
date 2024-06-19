package com.service.impl;

import com.dao.UserDao;
import com.entity.DTO.User;
import com.service.AuthorityDBService;
import org.springframework.beans.factory.annotation.Autowired;


public class AuthorityDBServiceImpl implements AuthorityDBService {

    @Autowired
    UserDao userDao;

}
