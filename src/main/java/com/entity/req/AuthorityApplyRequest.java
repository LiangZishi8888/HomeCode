package com.entity.req;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityApplyRequest {

    String adminUserId;
    
    String userId;

    List<String> expectGrants;
}
