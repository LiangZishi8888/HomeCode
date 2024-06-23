package com.demo.context;

import com.demo.entity.AuthCategoryEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryContext extends AccessCheckContext{

    private String userId;

    private String authName;

    private AuthCategoryEntity authCategoryEntity;
}
