package com.entity;

import com.constant.UserStatus;
import com.entity.req.AuthorityApplyRequest;
import com.entity.req.UserLoginRequest;
import com.util.DateUtils;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin {

    String userId;

    String accountName;

    String role;

    String lastLoginTime;

    String loginTime;

    UserStatus status;

    public static UserLogin createUserLogin(UserLoginRequest userLoginRequest) {
        return UserLogin.builder()
                .userId(userLoginRequest.getUserId())
                .role(userLoginRequest.getRole())
                .accountName(userLoginRequest.getAccountName())
                .loginTime(DateUtils.getCurrentDateStr())
                .build();
    }
}
