package com.entity.req;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    @NotNull
    String userId;

    @NotNull
    String accountName;

    String role;

}
