package com.entity.req;
import lombok.*;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestWithSign extends UserLoginRequest {

    @NotNull
    String signature;
}
