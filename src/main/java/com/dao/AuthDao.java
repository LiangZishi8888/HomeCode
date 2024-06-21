package com.dao;

import com.entity.DTO.AuthDTO;

public interface AuthDao {

    int updateAuth(AuthDTO authInDb);

    int insertAuthRecord(AuthDTO);
}
