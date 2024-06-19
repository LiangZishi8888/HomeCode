package com.mapper;

import com.entity.DTO.AuthInDb;
import com.entity.DTO.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    
    User findUserById(@Param("dbKey") int dbSplitKey,
                      @Param("uid") String userId);

    int updateUserLoginTimeById(@Param("dbKey") int dbSplitKey,
                                @Param("uid") String userId,
                                @Param("loginTime") String loginTime);

    List<AuthInDb> queryUserAuthsByUserId(@Param("dbKey") int dbSplitKey,
                                          @Param("uid") String userId,
                                          @Param("authName") String authName);
}
