package com.entity.DTO;

import com.constant.UserStatus;
import lombok.*;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.type.EnumTypeHandler;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    Integer dpSplitKey;

    String userId;

    String accountName;

    String createTime;

    /**
     * for modify user status
     */
    String lastUpdateTime;

    String lastLoginTime;

    String role;

    UserStatus status;

    @Override
    public String toString() {
        return "User{" +
                "dpSplitKey=" + dpSplitKey +
                ", userId='" + userId + '\'' +
                ", accountName='" + accountName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                ", role='" + role + '\'' +
                ", status=" + status +
                '}';
    }
}
