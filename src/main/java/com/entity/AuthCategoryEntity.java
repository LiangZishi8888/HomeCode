package com.entity;

import com.entity.DTO.AuthDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthCategoryEntity {

    private String authName;

    private boolean isUserHeld;

    private String authStatus;

    private AuthDTO authInDb;

    public static AuthCategoryEntity initAuthCategoryEntity(String authName){
        return AuthCategoryEntity.builder()
                .authName(authName)
                .isUserHeld(Boolean.FALSE)
                .build();
    }
}
