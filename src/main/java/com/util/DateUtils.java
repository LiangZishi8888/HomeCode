package com.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public abstract class DateUtils {

    public static final String DAY_MILLISECONDS="yyyy-MM-dd HH:mm:ss.SSS";

    public static final String DAY_SECONDS = "yyyy-MM-dd HH:mm:ss";

    public static final String DAY = "yyyy-MM-dd";

    private static DateTimeFormatter dfmDay = DateTimeFormatter.ofPattern(DAY_SECONDS);

    private static DateTimeFormatter dfmSeconds = DateTimeFormatter.ofPattern(DAY);

    public static Date getCurrentDate(){
        long currentTs=System.currentTimeMillis();
        return new Date(currentTs);
    }


    public static String getCurrentDateSecondsStr() {
        return getCurrentDateStrInternal(DAY_SECONDS);
    }

    public static String getCurrentDateStr() {
        return getCurrentDateStrInternal(DAY);
    }

    private static String getCurrentDateStrInternal(String format) {
        LocalDateTime currentTime = LocalDateTime.now();
        Assert.isTrue(StringUtils.equalsAny(format, DAY, DAY_SECONDS), "format is not support by system");
        switch (format) {
            case DAY_SECONDS:
                return dfmSeconds.format(currentTime);
            case DAY:
                return dfmDay.format(currentTime);
        }
        return null;
    }
}
