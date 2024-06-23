package com.entity;

import com.constant.AuthStatus;
import com.entity.DTO.AuthDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.h2.util.StringUtils;

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
public class AuthCategoryEntity {

    /**
     * the name of auth that user apply
     */
    private String authName;

    /**
     * the result after system access db whether user hold
     */
    @JsonIgnore
    private boolean isUserHeld;

    /**
     * the status of this auth
     * only auth exists effect
     * @see com.constant.UserStatus
     */
    private String authStatus;

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
       if(authCategoryEntity.isUserHeld()
               &&
               StringUtils.equals(authCategoryEntity.getAuthStatus(), AuthStatus.ACTIVE.getStatus()))
           return true;
       return false;
    }
}
