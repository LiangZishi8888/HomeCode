package com.entity;

import com.entity.DTO.AuthDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;

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
    private Date  grantTime;

    /**
     * the mapping dbEntity of this auth name
     * only auth exists effect
     */
    @JsonIgnore
    private AuthDTO authInDb;

    public static AuthCategoryEntity initAuthCategoryEntity(String authName){
        return AuthCategoryEntity.builder()
                .authName(authName)
                .isUserHeld(Boolean.FALSE)
                .build();
    }
}
