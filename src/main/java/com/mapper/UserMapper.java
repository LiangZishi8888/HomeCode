package com.mapper;

import com.entity.DTO.AuthDTO;
import com.entity.DTO.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

@Mapper
public interface UserMapper {
    
    UserDTO findUserById(@Param("dbKey") int dbSplitKey,
                         @Param("uid") String userId);

    int updateUserLoginTimeById(@Param("dbKey") int dbSplitKey,
                                @Param("uid") String userId,
                                @Param("loginTime") Date loginTime);
}
