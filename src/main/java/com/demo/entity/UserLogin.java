package com.demo.entity;

import com.demo.constant.UserRole;
import com.demo.constant.UserStatus;
import com.demo.entity.req.UserLoginRequest;
import com.demo.util.DateUtils;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * the mapping adpater class used for record user login action
 *  and record some info in db map back to context for build resp
 */
public class UserLogin {

    /**
     * the id of user who try to access this system
     */
    private String userId;

    /**
     * the name of the user
     */
    private String accountName;

    /**
     * the role of user
     * @see com.demo.constant.UserRole
     */
    private String role;

    /**
     * the user previous loginTime
     * if set means system has already query db
     */
    private Date lastLoginTime;

    /**
     * the time of the user currently access this time
     * set at very earliy stages in workFlow
     * no need query db
     */
    private Date loginTime;

    /**
     * status of user
     * @see UserStatus
     * also query from db
     */
    private UserStatus status;

    public static UserLogin createUserLogin(UserLoginRequest userLoginRequest) {
        return UserLogin.builder()
                .userId(userLoginRequest.getUserId())
                .role(userLoginRequest.getRole())
                .accountName(userLoginRequest.getAccountName())
                .loginTime(DateUtils.getCurrentDate())
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

    public static UserLogin buildUserLogin(String userId){
        return UserLogin.builder()
                .build();
    }
}
