package com.utils;

import com.demo.entity.crypto.CipherUtils;
import com.demo.entity.req.EncryptReq;
import com.demo.util.JsonUtil;
import org.junit.Assert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



public abstract class MockUtils {
    public static <R> R postPerform(MockMvc mockMvc, Object req, Class<R> respClass, String url,boolean isEncrypt){
        try {
            MvcResult mvcResult = mockMvc.perform(buildHttpPostReq(url, req,isEncrypt))
                    .andReturn();
            R r = JsonUtil.JsonToObj(mvcResult.getResponse().getContentAsString(), respClass);
            return r;
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
        return null;
    }


    public static<T> MockHttpServletRequestBuilder buildHttpPostReq(String url, Object req,boolean isEncrypt){
        String reqBody=JsonUtil.objToJson(req);
        System.out.println(reqBody);
        if(isEncrypt) {
            reqBody = CipherUtils.encryptWithSysKey(reqBody);
            reqBody = JsonUtil.objToJson(EncryptReq.builder()
                    .reqData(reqBody).build());
            System.out.println(reqBody);
        }
        return MockMvcRequestBuilders.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .content(reqBody)
                .contentType(MediaType.APPLICATION_JSON);
    }



    // you can mock signature self
    public static String getEncryptContent(String s){
        return CipherUtils.encryptWithSysKey(s);
    }

}
