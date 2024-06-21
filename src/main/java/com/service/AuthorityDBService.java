package com.service;

import com.entity.DTO.AuthDTO;

import java.util.List;

/**
 * this class is designed for the system after a checkpoint
 *   will execute a sequence of db insert update sql for transcationManagement
 *   with transcational ananotaion on  method defind in this class to control atomic
 */
public interface AuthorityDBService {

    void saveAuthsInDb(List<AuthDTO> authsInDb);
}
