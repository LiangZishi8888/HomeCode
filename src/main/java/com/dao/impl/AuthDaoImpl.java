package com.dao.impl;

import com.dao.AuthDao;
import com.entity.DTO.AuthDTO;
import com.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthDaoImpl implements AuthDao {

    @Autowired
    AuthMapper authMapper;

    @Override
    public int updateAuth(AuthDTO auth) {
        return 0;
    }

    @Override
    public int insertAuthRecord(AuthDTO auth) {
        return 0;
    }

    @Override
    public List<AuthDTO> getUserAuthsById(String userId) {
        return null;
    }
}
