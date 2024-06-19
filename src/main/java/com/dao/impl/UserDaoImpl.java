package com.dao.impl;

import com.constant.AuthException;
import com.constant.AuthDesc;
import com.dao.UserDao;
import com.entity.DTO.User;
import com.mapper.UserMapper;
import com.util.DRDSDbSplitKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserDaoImpl implements UserDao {

    @Autowired
    UserMapper userMapper;

    @Override
    public User findUserById(String userId,String accountName) {
        try{
            User user=userMapper.findUserById(DRDSDbSplitKeyUtils.calculateDbSplitKey(userId),
                    userId,accountName);
            return user;
        }catch (Exception e){
            log.error("query table t_u_auths_users failed",e);
            throw new AuthException(AuthDesc.DB_INTERNAL_ERROR);
        }
    }

    @Override
    public int updateLoginTimeById(String userId,String loginTime) {
        int successNum;
        try{
             successNum=userMapper.updateUserLoginTimeById(DRDSDbSplitKeyUtils.calculateDbSplitKey(userId),
                    userId,loginTime);
        }catch (Exception e){
            log.error("update table t_u_auths_users failed",e);
           throw new AuthException(AuthDesc.DB_INTERNAL_ERROR);
        }
        return successNum;
    }
}
