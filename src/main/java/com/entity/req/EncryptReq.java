package com.entity.req;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EncryptReq {
    private String reqData;
}
