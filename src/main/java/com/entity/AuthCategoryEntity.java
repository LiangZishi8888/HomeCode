package com.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthCategoryEntity {

    private String authName;

    private boolean isUserHeld;

    private boolean isStatusActive;

    // auth name whether is accpetable by the system
    private boolean isValid;

    public static AuthCategoryEntity createAuthCategoryEntity(String authName){
        return AuthCategoryEntity.builder()
                .authName(authName)
                .isUserHeld(Boolean.FALSE)
                .isStatusActive(Boolean.FALSE)
                .isValid(Boolean.FALSE)
                .build();
    }
}
