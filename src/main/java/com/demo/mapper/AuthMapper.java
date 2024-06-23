package com.demo.mapper;

import com.demo.entity.DTO.AuthDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthMapper {

    List<AuthDTO> queryUserAuthsByUserId(@Param("dbKey") int dbSplitKey,
                                         @Param("uid") String userId);

    AuthDTO queryUserAuthByUserIdAndAuthName(@Param("dbKey") int dbSplitKey,
                                             @Param("uid") String userId,
                                             @Param("authName") String categoryName);

    int  updateUserAuth(AuthDTO authDTO);

    int insertUserAuth(AuthDTO authDTO);
}
