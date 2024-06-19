package com.context;

import com.constant.AuthCategory;
import com.entity.AuthCategoryEntity;
import com.entity.GrantUserLogin;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthGrantAccessCheckContext {

    Boolean adminOnly;

    GrantUserLogin usersInfo;

    private List<AuthCategoryEntity> successAddedAuths;

    private List<AuthCategory> alreadyHoldAuths;

    private List<AuthCategoryEntity> expectedGrantAuths;

}
