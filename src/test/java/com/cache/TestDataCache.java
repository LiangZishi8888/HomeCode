package com.cache;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.Assert;
import com.demo.util.JsonUtil;

import java.io.File;

public class TestDataCache {

    private static final String location="src/test/MockRequests/mockReqs.json";

    private static final JsonNode root = JsonUtil.loadJsonFromFile(new File(location));

    public static <T> T getMockReq(String reqName,Class<T> clz){
        T t = (T) JsonUtil.JsonToObj(root.get(reqName).toString(), clz);
        Assert.notNull(t,"fetch named request from cache fail...");
        return t;
    }
}
