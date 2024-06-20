package com.entity.crypto;

import com.constant.AuthDesc;
import com.constant.AuthException;
import com.entity.req.EncryptReq;
import com.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;

@NoArgsConstructor
@AllArgsConstructor
public class PlainHttpInputMessage implements HttpInputMessage {

    private HttpHeaders httpHeaders;

    private InputStream body;

    private MethodParameter methodParameter;

    public PlainHttpInputMessage(HttpInputMessage inputMessage,MethodParameter methodParameter) throws IOException {
        this.httpHeaders=inputMessage.getHeaders();


        if(methodParameter.hasMethodAnnotation(PostMapping.class)){
        }else{
            this.body=inputMessage.getBody();
        }
    }

    @Override
    public InputStream getBody() throws IOException {
         return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }

}
