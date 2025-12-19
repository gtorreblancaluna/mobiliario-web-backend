package com.mx.gaby.mobiliario_web.utils;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.ValidationMessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class ValidateUtil {

    private ValidateUtil () {}

    public static void validateInitAndEndDate
            (final String initDate, final String endDate) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(ApplicationConstant.FORMAT_DATE_DD_MM_YYY);

        try {

            LocalDate initLocalDate = LocalDate.parse(initDate, formatter);
            LocalDate endLocalDate = LocalDate.parse(endDate, formatter);

            boolean initLocalDateIsBefore = initLocalDate.isBefore(endLocalDate);

            if (!initLocalDateIsBefore) {
                throw new BusinessException(
                        ValidationMessageConstant.END_DATE_IS_BEFORE_THAT_INIT_DATE);
            }

            LocalDate limitOneYear = initLocalDate.plusYears(1);

            if (endLocalDate.isAfter(limitOneYear)) {
                throw new BusinessException(
                        ValidationMessageConstant.LIMIT_ONE_YEAR_DATES);
            }

        } catch (DateTimeParseException e) {

            throw new BusinessException(
                    ValidationMessageConstant.FORMAT_DATE_DD_MM_YYYY_INVALID,
                    e.getCause());
        }

    }
}
