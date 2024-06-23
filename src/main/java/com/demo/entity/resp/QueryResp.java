package com.demo.entity.resp;

import com.demo.constant.AuthDesc;
import com.demo.context.QueryContext;
import com.demo.entity.AuthCategoryEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryResp extends BaseResp{

    private Boolean isExists;

    private AuthCategoryEntity authDetail;

    public static QueryResp createNotExistsAuthResp(){
       QueryResp queryResp= QueryResp.builder()
                .isExists(Boolean.FALSE)
                .build();
       queryResp.setResultCode(AuthDesc.SUCCESS.getResCode());
       queryResp.setResultDescription(AuthDesc.SUCCESS.getResDesc());
       return queryResp;
    }

    public static QueryResp buildSuccessQueryResp(QueryContext queryContext){
        QueryResp queryResp=QueryResp.builder()
                .isExists(Boolean.TRUE)
                .authDetail(queryContext.getAuthCategoryEntity())
                .build();
        queryResp.setResultCode(AuthDesc.SUCCESS.getResCode());
        queryResp.setResultDescription(AuthDesc.SUCCESS.getResDesc());
        return queryResp;
    }
}
