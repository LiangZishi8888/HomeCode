package com.demo.util;

import com.demo.constant.AuthCategory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AuthCategoryUtils {

    private static HashSet<String> authStrSet=new HashSet<>();

    static{
        List<String> authStrs = Arrays.stream(AuthCategory.values()).
                map(au -> au.getName()).collect(Collectors.toList());
        for(String s:authStrs){
            authStrSet.add(s);
        }
    }

    public static boolean isValidAuth(String authStr){
               return authStrSet.contains(authStr);
    }
}
