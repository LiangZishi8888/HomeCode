package com.mapper;

import com.entity.DTO.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    
    User findUserById(@Param("dbKey") int dbSplitKey,
                      @Param("uid") String userId,
                      @Param("acc") String accountName);

    int updateUserLoginTimeById(@Param("dbKey") int dbSplitKey,
                                @Param("uid") String userId,
                                @Param("loginTime") String loginTime);
}
