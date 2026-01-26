package com.mx.gaby.mobiliario_web.utils;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.ValidationMessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class ValidateUtil {

    private ValidateUtil () {}

    public static void isValidRange(final String timeRange) {

        if (timeRange == null || !timeRange.contains(ValidationMessageConstant.REGEX_A)) {
            throw new BusinessException(ValidationMessageConstant.HOURS_REQUIRED);
        }

        try {
            // Separamos el string: ["08:00", "18:00"]
            String[] parts = timeRange.split(ValidationMessageConstant.REGEX_A);
            if (parts.length != 2) {
                throw new BusinessException(ValidationMessageConstant.HOURS_REQUIRED);
            }

            LocalTime startTime = LocalTime.parse(parts[0].trim());
            LocalTime endTime = LocalTime.parse(parts[1].trim());

            // La hora inicial DEBE ser menor a la final
            if (!startTime.isBefore(endTime)) {
                throw new BusinessException(ValidationMessageConstant.INIT_HOUR_BE_BEFORE);
            }

        } catch (DateTimeParseException e) {
            // Si el formato de hora no es HH:mm (ej: 25:00 o letras)
            throw new BusinessException(
                    MessageFormat
                            .format(ValidationMessageConstant.ERROR_PARSING_INIT_END_HOURS,e.getMessage()));
        }
    }

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
