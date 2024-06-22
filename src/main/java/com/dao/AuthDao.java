package com.dao;

import com.entity.DTO.AuthDTO;

import java.util.List;

public interface AuthDao {

    int updateAuth(AuthDTO auth);

    int insertAuthRecord(AuthDTO auth);

    List<AuthDTO> getUserAuthsById(String userId);
}
