package com.entity;

import com.constant.UserStatus;
import com.entity.req.UserLoginRequest;
import com.util.DateUtils;
import lombok.*;

import java.sql.Date;

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
     * @see com.constant.UserRole
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
}
