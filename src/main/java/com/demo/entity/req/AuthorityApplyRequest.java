package com.demo.entity.req;

import lombok.*;

import javax.validation.constraints.NotNull;
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
    @NotNull
    private String adminUserId;

    /**
     * the id of User whom to be grant authorities
     */
    @NotNull
    private String userId;

    /**
     * a list name of the user expect to be grant auths
     * @see com.demo.constant.AuthCategory
     */
    @NotNull
    private List<String> expectGrants;
}
