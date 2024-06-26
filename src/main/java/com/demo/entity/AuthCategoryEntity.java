package com.demo.entity;

import com.demo.constant.AuthStatus;
import com.demo.entity.DTO.AuthDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
/**
 * an adapter class using to linker from grantApply request to context
 */
@JsonIgnoreProperties(value = "isUserHeld")
public class AuthCategoryEntity {

    /**
     * the name of auth that user apply
     */
    private String authName;

    /**
     * the result after system access db whether user hold
     */
    private Boolean isUserHeld;

    /**
     * the auth association no
     */
    private String associationNo;

    /**
     * the status of this auth
     * only auth exists effect
     * @see com.demo.constant.AuthStatus
     */
    private AuthStatus authStatus;

    /**
     * the previous grant UserId if capable
     */
    private String previousAdminUserId;

    /**
     * the previous grant UserName if capable
     */
    private String previousAdminUserName;

    /**
     * the previous auth status if capable
     */
    private AuthStatus previousAuthStatus;

    /**
     * the previous grantDate if capable
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
    private Date previousGrantDate;

    /**
     * to map the time of auths grant back to resp
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
    private Date  grantTime;


    public static AuthCategoryEntity initAuthCategoryEntity(String authName){
        return AuthCategoryEntity.builder()
                .authName(authName)
                .isUserHeld(Boolean.FALSE)
                .build();
    }

    public static boolean isUserHoldActiveAuth(AuthCategoryEntity authCategoryEntity){
        // no need grant user currently hold avtive auths
       if(authCategoryEntity.getIsUserHeld() && Objects.equals(authCategoryEntity.getAuthStatus(),AuthStatus.ACTIVE))
           return true;
       return false;
    }

    public static void mapAuthDtoBackToEntity(AuthCategoryEntity authCategoryEntity, AuthDTO auth){
        authCategoryEntity.setAuthStatus(auth.getStatus());
        authCategoryEntity.setGrantTime(auth.getCreateTime());
        authCategoryEntity.setAssociationNo(auth.getAuthAssociationId());
        authCategoryEntity.setPreviousGrantDate(auth.getCreateTime());
        authCategoryEntity.setPreviousAdminUserId(auth.getAdminUserId());
        authCategoryEntity.setPreviousAdminUserName(auth.getAdminUserName());
        authCategoryEntity.setPreviousAuthStatus(auth.getStatus());
    }
}
