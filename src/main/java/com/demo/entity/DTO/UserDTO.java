package com.demo.entity.DTO;

import com.demo.constant.UserStatus;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * mapping entity of table `t_u_auth_users`
 */
public class UserDTO {

    /**
     * for drds
     */
    private Integer dpSplitKey;

    /**
     * the id of login user
     */
    private String userId;

    /**
     * name of user
     */
    private String accountName;

    /**
     * the time this user create
     * this field is insert by register interface
     */
    private Date createTime;

    /**
     * for modify user status
     * usually the adminUser modify this
     */
    private Date lastUpdateTime;

    /**
     * the time that system record user last access time
     */
    private Date lastLoginTime;

    /**
     * role of user
     * @see com.demo.constant.UserRole
     */
    private String role;

    /**
     * status of user
     * @see com.demo.constant.AuthStatus
     */
    private UserStatus status;

}
