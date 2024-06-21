package com.context;

import com.entity.DTO.UserDTO;
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

    private String accessTime;

    private List<UserDTO> users;
}
