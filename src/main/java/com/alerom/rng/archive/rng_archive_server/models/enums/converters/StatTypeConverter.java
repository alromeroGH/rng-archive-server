package com.alerom.rng.archive.rng_archive_server.models.enums.converters;

import com.alerom.rng.archive.rng_archive_server.models.enums.StatTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatTypeConverter implements AttributeConverter<StatTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(StatTypeEnum attribute) {
        return (attribute == null) ? null : attribute.getValue();
    }

    @Override
    public StatTypeEnum convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : StatTypeEnum.fromValue(dbData);
    }
}