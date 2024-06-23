package com.demo.entity;

import com.demo.entity.req.AuthorityApplyRequest;
import com.demo.util.DateUtils;
import lombok.*;

import java.util.Date;

/**
 *  a GrantUserLogin
 *  contains adminUser and grantUser id
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * a mapping adapter field to mapp info from admin and user to be grant authority
 */
public class GrantUserLogin {

    /**
     * the id of adminUser
     */
    private String adminUserId;

    /**
     * the id of user whom to be grant
     */
    private String userId;

    /**
     * the time that admin user access the system
     */
    private Date accessTime;

    /**
     * the name of the user whom to be grant
     */
    private String userName;

    /**
     * the name of admin user
     */
    private String adminUserName;

    /**
     * the status of user
     * @see com.demo.constant.UserStatus
     */
    private String userStatus;

    /**
     * the satus of adminUser
     * @see com.demo.constant.UserStatus
     */
    private String adminUserStatus;

    public static GrantUserLogin createGrantUserLogin(AuthorityApplyRequest authorityApplyRequest) {
        return GrantUserLogin.builder()
                .userId(authorityApplyRequest.getUserId())
                .adminUserId(authorityApplyRequest.getAdminUserId())
                .accessTime(DateUtils.getCurrentDate())
                .build();
    }
}
