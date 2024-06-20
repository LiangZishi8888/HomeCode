package com.context;

import com.constant.AuthCategory;
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

    private GrantUserLogin usersInfo;

    AuthorityApplyRequest authorityApplyRequest;

    private List<AuthCategoryEntity> successAddedAuths;

    private List<AuthCategoryEntity> userHoldAuths;

    private List<AuthCategoryEntity> expectedGrantAuths;

}
