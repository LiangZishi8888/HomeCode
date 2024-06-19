package com.context;

import com.entity.DTO.User;
import com.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccessCheckContext {

    Boolean checkResult;

    Boolean adminOnly;

    UserLogin userLogin;

    User user;
}
