package com.entity.resp;

import com.Flow.AdminGrantAccessWorkFlow;
import com.constant.AuthDesc;
import com.context.AuthGrantAccessCheckContext;
import com.entity.AuthCategoryEntity;
import com.entity.GrantUserLogin;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * the interface resp of grant auth
 * @see com.Flow.AdminGrantAccessWorkFlow
 */
public class AuthGrantResp extends BaseResp{
    /**
     * the id of adminUser
     */
    private String grantUserId;

    /**
     * the name of adminUser
     */
    private String grantUserName;

    /**
     * the id of user who apply this auth
     */
    private String userId;

    /**
     * the name of user who apply this auth
     */
    private String userName;

    /**
     * the count of successfully grant
     */
    private Integer successCount;

    /**
     * grants of successful auths in db
     */
    private List<AuthCategoryEntity> successGrants;

    /**
     * to map user hold active grants in expect grants
     */
    private List<AuthCategoryEntity> userHoldActive;

    /**
     * the time to grant authority
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date grantTime;

}
