package com.demo.dao;

import com.demo.entity.DTO.AuthDTO;

import java.util.List;

public interface AuthDao {

    int updateAuth(AuthDTO auth);

    int insertAuthRecord(AuthDTO auth);

    List<AuthDTO> getUserAuthsById(String userId);

    AuthDTO  getUserAuthByIdAuthName(String userId,String authName);
}
