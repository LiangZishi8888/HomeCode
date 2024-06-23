package com.demo.entity.resp;

import com.demo.entity.AuthCategoryEntity;
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
 * @see com.demo.Flow.AdminGrantAccessWorkFlow
 */
public class AuthGrantResp extends BaseResp{
    /**
     * the id of adminUser
     */
    private String adminUserId;

    /**
     * the name of adminUser
     */
    private String adminUserName;

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
    private List<AuthCategoryEntity> userPreviousHoldActive;

    /**
     * the time to grant authority
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS",timezone = "GMT+8")
    private Date grantTime;

}
