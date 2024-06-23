package com.demo.dao.impl;

import com.demo.constant.AuthException;
import com.demo.constant.AuthDesc;
import com.demo.dao.UserDao;
import com.demo.entity.DTO.AuthDTO;
import com.demo.entity.DTO.UserDTO;
import com.demo.mapper.UserMapper;
import com.demo.util.DRDSDbSplitKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class UserDaoImpl implements UserDao {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDTO findUserById(String userId) {
        try{
            UserDTO user=userMapper.findUserById(DRDSDbSplitKeyUtils.calculateDbSplitKey(userId),
                    userId);
            return user;
        }catch (Exception e){
            log.error("query table t_u_auths_users failed",e);
            throw new AuthException(AuthDesc.DB_INTERNAL_ERROR,userId);
        }
    }

    @Override
    public int updateLoginTimeById(String userId, Date loginTime) {
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

}
