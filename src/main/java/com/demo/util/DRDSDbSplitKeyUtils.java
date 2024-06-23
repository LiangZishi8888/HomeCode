package com.demo.util;

import org.h2.util.StringUtils;
import org.springframework.util.Assert;

public class DRDSDbSplitKeyUtils {

    // it is a mock method for calculate DbSplitKey
    public static int calculateDbSplitKey(String content){
        // This should valid By hibernate
        Assert.isTrue(StringUtils.isNumber(content),"invalid content");
        Integer dbSplitKey=Integer.valueOf(content)<<1;
        return  dbSplitKey;
    }
}
