package com.context;

import com.entity.DTO.User;
import com.entity.GrantUserLogin;
import com.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccessCheckContext {

    private Boolean checkResult;

    private Boolean adminOnly;

    private UserLogin userLogin;

    private List<User> users;
}
