package com.utils;

import com.entity.crypto.CipherUtils;
import com.util.JsonUtil;
import org.junit.Assert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



public abstract class MockUtils {
    public static <R> R postPerform(MockMvc mockMvc, Object req, Class<R> respClass, String url){
        try {
            MvcResult mvcResult = mockMvc.perform(buildHttpPostReq(url, req))
                    .andReturn();
            R r = JsonUtil.JsonToObj(mvcResult.getResponse().getContentAsString(), respClass);
            return r;
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
        return null;
    }
    public static<T> MockHttpServletRequestBuilder buildHttpPostReq(String url, Object req){
        String reqBody=JsonUtil.objToJson(req);
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
