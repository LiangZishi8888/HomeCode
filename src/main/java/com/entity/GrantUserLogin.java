package com.entity;

import com.constant.UserRole;
import com.entity.req.AuthorityApplyRequest;
import com.util.DateUtils;
import lombok.*;

/**
 *  a GrantUserLogin
 *  contains adminUser and grantUser id
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrantUserLogin {

    String adminUserId;

    String userId;

    String accessTime;

    String userName;

    String adminUserName;

    public static GrantUserLogin createGrantUserLogin(AuthorityApplyRequest authorityApplyRequest) {
        return GrantUserLogin.builder()
                .userId(authorityApplyRequest.getUserId())
                .adminUserId(authorityApplyRequest.getAdminUserId())
                .accessTime(DateUtils.getCurrentDateSecondsStr())
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
