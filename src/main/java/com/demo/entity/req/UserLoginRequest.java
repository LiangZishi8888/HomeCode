package com.demo.entity.req;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * mapping class of userLoginRequest
 */
public class UserLoginRequest {

    /**
     * the userId of the user who try to access the system
     */
    @NotNull
    String userId;

    /**
     * the accountName of user
     */
    @NotNull
    String accountName;

    /**
     * role of user
     * @see com.demo.constant.UserRole
     */
    @NotNull
    String role;

}
