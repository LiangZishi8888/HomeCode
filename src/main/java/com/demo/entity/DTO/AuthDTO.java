package com.demo.entity.DTO;

import com.demo.constant.AuthStatus;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * mapping enetity of jdbc table `t_u_auths_grant_details`
 */
public class AuthDTO {

    /**
     *  for drds
     */
    private Integer dbSplitKey;

    /**
     *  primary key
     * @see com.demo.generator.impl.AuthAssoNoGenerator
     */
    private String authAssociationId;

    /**
     * the userId of whom to be granted authority
     */
    private String userId;

    /**
     * name of user
     */
    private String userName;

    /**
     *  the user id of admin
     */
    private String adminUserId;

    /**
     * name of adminUser
     */
    private String adminUserName;

    /**
     * the time that this grant record occurs
     * only first grant execute insert sql will generate
     */
    private Date createTime;

    /**
     * last time modify this record
     */
    private Date lastModifyTime;

    /**
     * auth name of this auth
     * @see com.demo.constant.AuthCategory
     */
    private String authCategory;

    /**
     * auth status
     * @see com.demo.constant.AuthStatus
     */
    private AuthStatus status;
}
