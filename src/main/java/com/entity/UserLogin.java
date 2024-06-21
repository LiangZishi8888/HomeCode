package com.entity;

import com.constant.UserStatus;
import com.entity.req.UserLoginRequest;
import com.util.DateUtils;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin {

    private String userId;

    private String accountName;

    private String role;

    private String lastLoginTime;

    private String loginTime;

    private UserStatus status;

    public static UserLogin createUserLogin(UserLoginRequest userLoginRequest) {
        return UserLogin.builder()
                .userId(userLoginRequest.getUserId())
                .role(userLoginRequest.getRole())
                .accountName(userLoginRequest.getAccountName())
                .loginTime(DateUtils.getCurrentDateSecondsStr())
                .build();
    }
}
