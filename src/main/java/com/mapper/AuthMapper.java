package com.mapper;

import com.entity.DTO.AuthDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthMapper {

    List<AuthDTO> queryUserAuthsByUserId(@Param("dbSplitKey") int dbSplitKey,
                                         @Param("uid") String userId);

    AuthDTO queryUserAuthByUserIdAndAuthName(@Param("dbSplitKey") int dbSplitKey,
                                             @Param("uid") String userId,
                                             @Param("authName") String categoryName);
}
