package com.entity;

import com.constant.UserRole;
import com.entity.req.AuthorityApplyRequest;
import com.util.DateUtils;
import lombok.*;

import java.sql.Date;

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
    String adminUserId;

    /**
     * the id of user whom to be grant
     */
    String userId;

    /**
     * the time that admin user access the system
     */
    Date accessTime;

    /**
     * the name of the user whom to be grant
     */
    String userName;

    /**
     * the name of admin user
     */
    String adminUserName;

    public static GrantUserLogin createGrantUserLogin(AuthorityApplyRequest authorityApplyRequest) {
        return GrantUserLogin.builder()
                .userId(authorityApplyRequest.getUserId())
                .adminUserId(authorityApplyRequest.getAdminUserId())
                .accessTime(DateUtils.getCurrentDate())
                .build();
    }

    // this can also implement by BeanUtils.copyProperties
    // Since we need reuse userservice.checkRoleStatus()
    public static UserLogin buildUserLogin(GrantUserLogin grantUserLogin, boolean isAdmin) {
        String userId = isAdmin ? grantUserLogin.getAdminUserId() : grantUserLogin.getUserId();
        String role = isAdmin ? UserRole.ADMIN.getRole() : UserRole.USER.getRole();
        return UserLogin.builder()
                .userId(userId)
                .role(role)
                .build();
    }
}
