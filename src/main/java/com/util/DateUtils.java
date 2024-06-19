package com.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateUtils {

    private static DateTimeFormatter dfm=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentDateStr(){
        String format = dfm.format(LocalDateTime.now());
        return format;
    }
}
