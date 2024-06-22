package com.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public abstract class DateUtils {

    private static final String SECONDS = "yyyy-MM-dd HH:mm:ss";

    private static final String DAY = "yyyy-MM-dd";

    private static DateTimeFormatter dfmDay = DateTimeFormatter.ofPattern(SECONDS);

    private static DateTimeFormatter dfmSeconds = DateTimeFormatter.ofPattern(DAY);

    public static Date getCurrentDate(){
        long currentTs=System.currentTimeMillis();
        return new java.sql.Date(currentTs);
    }


    public static String getCurrentDateSecondsStr() {
        return getCurrentDateStrInternal(SECONDS);
    }

    public static String getCurrentDateStr() {
        return getCurrentDateStrInternal(DAY);
    }

    private static String getCurrentDateStrInternal(String format) {
        LocalDateTime currentTime = LocalDateTime.now();
        Assert.isTrue(StringUtils.equalsAny(format, DAY, SECONDS), "format is not support by system");
        switch (format) {
            case SECONDS:
                return dfmSeconds.format(currentTime);
            case DAY:
                return dfmDay.format(currentTime);
        }
        return null;
    }
}
