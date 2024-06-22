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
/**
 * the check context of interface /user/grant
 */
public class AuthGrantAccessCheckContext extends AccessCheckContext{

    /**
     * used for divdied into 2 userLogin entity for data base query
     * and map some information back to context
     * @see AuthGrantAccessCheckContext
     */
    private GrantUserLogin usersInfo;

    /**
     * to store an reference to the orginal requestBody
     */
    AuthorityApplyRequest authorityApplyRequest;

    /**
     * possible grant auths --this means if no database exception throw
     *   all the authCategory will successfully grant
     */
    private List<AuthCategoryEntity> possibleGrantAuths;

    /**
     * the mappings of the user expect grants in orginal requestBody
     */
    private List<AuthCategoryEntity> expectedGrantAuths;

}
