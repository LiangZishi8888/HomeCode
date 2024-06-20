package com.entity.resp;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResp<T> {

    private String resultCode;

    private String resultDescription;

    private String errorMsg;

    T respData;
}
