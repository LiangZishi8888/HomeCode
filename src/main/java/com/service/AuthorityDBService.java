package com.service;

import com.entity.DTO.User;

public interface AuthorityDBService {

    User findUserById(String id,String accountName);
}
