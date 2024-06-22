package com.entity.req;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * the mapping class of authortiy apply request
 */
public class AuthorityApplyRequest {

    /**
     * the id of adminUser
     */
    private String adminUserId;

    /**
     * the id of User whom to be grant authorities
     */
    private String userId;

    /**
     * a list name of the user expect to be grant auths
     * @see com.constant.AuthCategory
     */
    private List<String> expectGrants;
}
