package com.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateUtils {

    private static final String SECONDS = "yyyy-MM-dd HH:mm:ss";

    private static final String DAY = "yyyy-MM-dd";

    private static DateTimeFormatter dfmDay = DateTimeFormatter.ofPattern(SECONDS);

    private static DateTimeFormatter dfmSeconds = DateTimeFormatter.ofPattern(DAY);


    public static String getCurrentDateSecondsStr() {
        return getCurrentDateStrInternal(SECONDS);
    }

    public static String getCurrentDateStr() {
        return getCurrentDateStrInternal(DAY);
    }

    private static String getCurrentDateStrInternal(String format) {
        Assert.isTrue(StringUtils.equalsAny(format, DAY, SECONDS), "format is not support by system");
        LocalDateTime currentTime = LocalDateTime.now();
        switch (format) {
            case SECONDS:
                return dfmSeconds.format(currentTime);
            case DAY:
                return dfmDay.format(currentTime);
        }
        return null;
    }
}
