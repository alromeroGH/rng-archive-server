package com.alerom.rng.archive.rng_archive_server.models.enums.converters;

import com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NumberOfStarsConverter implements AttributeConverter<NumberOfStarsEnum, String> {

    @Override
    public String convertToDatabaseColumn(NumberOfStarsEnum attribute) {
        return (attribute == null) ? null : attribute.getValue();
    }

    @Override
    public NumberOfStarsEnum convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : NumberOfStarsEnum.fromValue(dbData);
    }
}