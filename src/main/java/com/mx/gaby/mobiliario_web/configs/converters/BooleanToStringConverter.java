package com.mx.gaby.mobiliario_web.configs.converters;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) return ApplicationConstant.ZERO;
        return attribute ? ApplicationConstant.ONE : ApplicationConstant.ZERO;
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return ApplicationConstant.ONE.equals(dbData);
    }
}