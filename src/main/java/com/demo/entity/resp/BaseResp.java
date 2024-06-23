package com.demo.entity.resp;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
/**
 * common parent class of response
 */
public class BaseResp {

    /**
     * the result code return to terminal
     * @see com.demo.constant.AuthDesc
     */
    private String resultCode;

    /**
     * the result desc return to terminal
     * @see com.demo.constant.AuthDesc
     */
    private String resultDescription;

    /**
     * addtional msg if interface return expection if necessary
     * @see com.demo.constant.AuthException
     */
    private String errorMsg;
}
