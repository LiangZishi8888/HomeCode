package com.demo.context;

import com.demo.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
     * map some information for user access and adapt to workflow
     */
    private UserLogin userLogin;

}
