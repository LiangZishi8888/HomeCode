package com.entity.resp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BaseResp {

    private String resultCode;

    private String resultDescription;
}
