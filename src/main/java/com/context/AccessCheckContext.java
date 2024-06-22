package com.context;

import com.entity.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
/**
 * the parent class of check context
 */
public class AccessCheckContext {

    /**
     *  to identity whether current access only require admin
     */
    private Boolean adminOnly;

    /**
     * record the access time of system
     */
    private Date accessTime;

    /**
     *
     */
    private List<UserDTO> users;
}
