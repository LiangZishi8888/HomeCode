package com.demo.entity.crypto;

import com.demo.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class PlainHttpInputMessage implements HttpInputMessage {

    private static final String encryptPropertyName="reqData";

    private HttpHeaders httpHeaders;

    private InputStream body;

    private MethodParameter methodParameter;

    public PlainHttpInputMessage(HttpInputMessage inputMessage,MethodParameter methodParameter) throws IOException {
        this.httpHeaders=inputMessage.getHeaders();
        if(methodParameter.hasMethodAnnotation(PostMapping.class)
            &&methodParameter.hasMethodAnnotation(DecryptRequest.class)){
            String encryptData = JsonUtil.getPropertyValueFromJson( IOUtils.toString(
                    inputMessage.getBody(), "UTF-8"),
                    encryptPropertyName);
            log.info("successfully intercept request, encryptData: ",encryptData);
            this.body= IOUtils.toInputStream(CipherUtils.decryptWithSysKey(encryptData),
                    "UTF-8");
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
