package com.entity.resp;

import com.entity.AuthCategoryEntity;
import lombok.*;

import java.util.List;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthGrantResp extends BaseResp{

    private List<AuthCategoryEntity> successGrants;
}
