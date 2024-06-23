package com.demo.dao.impl;

import com.demo.constant.AuthDesc;
import com.demo.constant.AuthException;
import com.demo.dao.AuthDao;
import com.demo.entity.DTO.AuthDTO;
import com.demo.mapper.AuthMapper;
import com.demo.util.DRDSDbSplitKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AuthDaoImpl implements AuthDao {

    @Autowired
    AuthMapper authMapper;

    @Override
    public int updateAuth(AuthDTO auth) {
        int successNum;
        try{
            successNum=authMapper.updateUserAuth(auth);
            return successNum;
        }catch (Exception e){
            log.error("update auth record failed,userId:{},authName:{},msg:{}",
                    auth.getUserId(),auth.getAuthCategory(),e);
            throw new AuthException(AuthDesc.DB_INTERNAL_ERROR,null);
        }
    }

    @Override
    public int insertAuthRecord(AuthDTO auth) {
        int successNum;
        try{
            successNum = authMapper.insertUserAuth(auth);
            return successNum;
        }catch (Exception e){
            log.error("save auth failed.. userId:{},authName:{}",auth.getUserId(),auth.getAuthCategory());
            throw new AuthException(AuthDesc.DB_INTERNAL_ERROR,null);
        }
    }

    @Override
    public List<AuthDTO> getUserAuthsById(String userId) {
        try {
            List<AuthDTO> auths = authMapper.queryUserAuthsByUserId(
                    DRDSDbSplitKeyUtils.calculateDbSplitKey(userId), userId);
            return auths;
        }catch (Exception e){
            log.error("query auth table failed,userId:{},msg:{}",userId,e);
            throw new AuthException(AuthDesc.DB_INTERNAL_ERROR,null);
        }
    }

    @Override
    public AuthDTO getUserAuthByIdAuthName(String userId, String authName) {
        try{
            return authMapper.queryUserAuthByUserIdAndAuthName(
                    DRDSDbSplitKeyUtils.calculateDbSplitKey(userId),userId,authName);
        }catch (Exception e){
            log.error("query user auth failed,userid:{},authName:{}",userId,authName,e);
            throw new AuthException(AuthDesc.DB_INTERNAL_ERROR,null);
        }
    }
}
