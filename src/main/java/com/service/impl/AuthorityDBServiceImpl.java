package com.service.impl;

import com.dao.UserDao;
import com.entity.DTO.AuthDTO;
import com.service.AuthService;
import com.service.AuthorityDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  all the method in this class requires transcationControl
 *  designer for mutiple insert or update sql in the workFlow operation
 */
public class AuthorityDBServiceImpl implements AuthorityDBService {

    @Autowired
    AuthService authDao;

    @Autowired
    UserDao  userDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAuthsInDb(List<AuthDTO> authsInDb) {

    }
}
