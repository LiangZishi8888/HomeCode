package com.context;

import com.entity.AuthCategoryEntity;
import com.entity.GrantUserLogin;
import com.entity.req.AuthorityApplyRequest;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthGrantAccessCheckContext extends AccessCheckContext{

    Boolean grantUserCheckResult;

    /**
     * used for divdied into 2 userLogin entity for data base query
     */
    private GrantUserLogin usersInfo;

    AuthorityApplyRequest authorityApplyRequest;

    private List<AuthCategoryEntity> possibleGrantAuths;

    private List<AuthCategoryEntity> expectedGrantAuths;

}
