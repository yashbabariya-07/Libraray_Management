package com.lbm.utils;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mapstruct.Named;

import java.util.Date;


public class AppTimeZoneUtil {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm:ss");

    public static Date toUTCDateTime(LocalDateTime localDateTime, String userTimeZone) {
        DateTimeZone userZone = DateTimeZone.forID(userTimeZone);
        DateTime userDateTime = localDateTime.toDateTime(userZone);
        DateTime utcDateTime = userDateTime.withZone(DateTimeZone.UTC);
        return utcDateTime.toDate();
    }

    public static String toUTC(LocalTime localTime, String userTimeZone) {
        DateTimeZone userZone = DateTimeZone.forID(userTimeZone);
        DateTime localDateTime = LocalDate.now().toDateTime(localTime, userZone);
        DateTime utcDateTime = localDateTime.withZone(DateTimeZone.UTC);
        return utcDateTime.toLocalTime().toString(TIME_FORMATTER);
    }

    public static LocalTime toUserTime(String utcTimeStr, String userTimeZone) {
        LocalTime utcTime = LocalTime.parse(utcTimeStr, TIME_FORMATTER);
        DateTimeZone userZone = DateTimeZone.forID(userTimeZone);
        DateTime utcDateTime = LocalDate.now().toDateTime(utcTime, DateTimeZone.UTC);
        DateTime userDateTime = utcDateTime.withZone(userZone);
        return userDateTime.toLocalTime();
    }

    @Named("localTimeToString")
    public static String stringFormat(LocalTime localTime){
        return TIME_FORMATTER.print(localTime);
    }

    @Named("stringToLocalTime")
    public static LocalTime parseStringFormat(String timeStr){
        return TIME_FORMATTER.parseLocalTime(timeStr);
    }
}
