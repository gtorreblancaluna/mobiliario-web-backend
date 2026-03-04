package com.mx.gaby.mobiliario_web.utils;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private DateUtil () {}

    public static String getStringTodayDateFormatDatabase () {

        DateTimeFormatter formatter
                = DateTimeFormatter
                .ofPattern(ApplicationConstant.FORMAT_DATE_DD_MM_YYY);

        return LocalDate
                .now(ZoneId.of(ApplicationConstant.ZONE_ID_MX))
                .format(formatter);
    }
}
