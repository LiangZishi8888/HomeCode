package com.mapper;

import com.entity.DTO.AuthDTO;
import com.entity.DTO.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    
    UserDTO findUserById(@Param("dbKey") int dbSplitKey,
                         @Param("uid") String userId);

    int updateUserLoginTimeById(@Param("dbKey") int dbSplitKey,
                                @Param("uid") String userId,
                                @Param("loginTime") String loginTime);

    List<AuthDTO> queryUserAuthsByUserId(@Param("dbKey") int dbSplitKey,
                                         @Param("uid") String userId,
                                         @Param("authName") String authName);
}
