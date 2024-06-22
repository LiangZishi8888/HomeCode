package com.entity.DTO;

import com.constant.UserStatus;
import lombok.*;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.type.EnumTypeHandler;

import java.sql.Date;

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
    Integer dpSplitKey;

    /**
     * the id of login user
     */
    String userId;

    /**
     * name of user
     */
    String accountName;

    /**
     * the time this user create
     * this field is insert by register interface
     */
    Date createTime;

    /**
     * for modify user status
     * usually the adminUser modify this
     */
    Date lastUpdateTime;

    /**
     * the time that system record user last access time
     */
    Date lastLoginTime;

    /**
     * role of user
     * @see com.constant.UserRole
     */
    String role;

    /**
     * status of user
     * @see com.constant.AuthStatus
     */
    UserStatus status;

}
