package com.zjl.paas.service.util;

import java.time.Instant;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-10-28
 * @Version: 1.0
 */
public class DateUtil {

    public static long getTimeInMS() {
        return Instant.now().toEpochMilli();
    }

    public static long getTimeInMS(Instant instant) {
        return instant.toEpochMilli();
    }

    public static long dateDiff(final Instant endDate, final Instant startDate) {
        return (getTimeInMS(endDate) - getTimeInMS(startDate));
    }

    public static long dateDiff(final Instant endDate, final long startTimeInMS) {
        return (getTimeInMS(endDate) - startTimeInMS);
    }

    public static long dateDiff(final String endDate, final String startDate) {
        return (getTimeInMS(getInstant(endDate)) - getTimeInMS(getInstant(startDate)));
    }

    public static Instant getInstantFromMS(final long timeInMS) {
        return Instant.ofEpochMilli(timeInMS);
    }

    public static Instant getInstant(final String date) {
        return Instant.parse(date);
    }
}
