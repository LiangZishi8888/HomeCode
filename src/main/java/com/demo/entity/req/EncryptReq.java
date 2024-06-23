package com.demo.entity.req;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
/**
 * mapping class for requestBody with encryptRequest
 */
public class EncryptReq {

    private String reqData;
}
