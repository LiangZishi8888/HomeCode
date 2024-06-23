package com.dao.impl;

import com.constant.AuthDesc;
import com.constant.AuthException;
import com.dao.AuthDao;
import com.entity.DTO.AuthDTO;
import com.mapper.AuthMapper;
import com.util.DRDSDbSplitKeyUtils;
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
        return 0;
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
}
