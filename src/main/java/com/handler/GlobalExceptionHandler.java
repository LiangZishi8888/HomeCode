package com.handler;

import com.constant.AuthException;

import com.entity.resp.BaseResp;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Component
public class GlobalExceptionHandler{


    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public BaseResp resolveException(Exception e) {
        AuthException contollerEx=(AuthException) e;
         BaseResp resp=new BaseResp();
         resp.setResultCode(contollerEx.getResultCode());
         resp.setResultDescription(contollerEx.getResultDescription());
         resp.setErrorMsg(contollerEx.getErrorMsg());
         return resp;
    }
}
