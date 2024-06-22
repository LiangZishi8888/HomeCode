package com.service.impl;

import com.constant.AuthDesc;
import com.constant.AuthException;
import com.dao.AuthDao;
import com.dao.UserDao;
import com.entity.DTO.AuthDTO;
import com.service.AuthorityDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


/**
 *  all the method in this class requires transcationControl
 *  designer for mutiple insert or update sql in the workFlow operation
 */
@Slf4j
@Component
public class AuthorityDBServiceImpl implements AuthorityDBService {

    @Autowired
    AuthDao authDao;

    @Autowired
    UserDao  userDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAuthsInDb(List<AuthDTO> authsInDb) {
            for (AuthDTO auth : authsInDb) {
                try {
                    if (Objects.isNull(auth.getCreateTime()))
                        authDao.updateAuth(auth);
                    else
                        authDao.insertAuthRecord(auth);
                }catch (Exception e){
                     log.error("save auth failed,userId:{},authName:{}",auth.getUserId(),auth.getAuthCategory());
                     throw new AuthException(AuthDesc.AUTH_GRANT_FAILED,auth.getAuthCategory());
                }
            }
    }
}
