package com.entity.resp;

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
     * @see com.constant.AuthDesc
     */
    private String resultCode;

    /**
     * the result desc return to terminal
     * @see com.constant.AuthDesc
     */
    private String resultDescription;

    /**
     * addtional msg if interface return expection if necessary
     * @see com.constant.AuthException
     */
    private String errorMsg;
}
