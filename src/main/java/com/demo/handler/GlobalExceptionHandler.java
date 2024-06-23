package com.demo.handler;

import com.demo.constant.AuthDesc;
import com.demo.constant.AuthException;

import com.demo.entity.resp.BaseResp;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Component
public class GlobalExceptionHandler{


    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public BaseResp resolveException(Exception e) {
        AuthException controllerEx;
        if(!(e instanceof AuthException))
            controllerEx = new AuthException(AuthDesc.SYS_INTERNAL_ERROR, e.getMessage());
        else
             controllerEx=(AuthException) e;
         BaseResp resp=new BaseResp();
         resp.setResultCode(controllerEx.getResultCode());
         resp.setResultDescription(controllerEx.getResultDescription());
         resp.setErrorMsg(controllerEx.getErrorMsg());
         return resp;
    }
}
