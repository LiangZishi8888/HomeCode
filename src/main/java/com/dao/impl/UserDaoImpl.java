package com.dao.impl;

import com.constant.AuthException;
import com.constant.AuthDesc;
import com.dao.UserDao;
import com.entity.DTO.AuthInDb;
import com.entity.DTO.User;
import com.mapper.UserMapper;
import com.util.DRDSDbSplitKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserDaoImpl implements UserDao {

    @Autowired
    UserMapper userMapper;

    @Override
    public User findUserById(String userId) {
        try{
            User user=userMapper.findUserById(DRDSDbSplitKeyUtils.calculateDbSplitKey(userId),
                    userId);
            return user;
        }catch (Exception e){
            log.error("query table t_u_auths_users failed",e);
            throw new AuthException(AuthDesc.DB_INTERNAL_ERROR,null);
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
           throw new AuthException(AuthDesc.DB_INTERNAL_ERROR,null);
        }
        return successNum;
    }

    @Override
    public List<AuthInDb> queryUserAuths(String userId) {
        return null;
    }
}
