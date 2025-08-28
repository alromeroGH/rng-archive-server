package com.alerom.rng.archive.rng_archive_server.models.enums.converters;

import com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UnitTypeConverter implements AttributeConverter<UnitTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(UnitTypeEnum attribute) {
        return (attribute == null) ? null : attribute.getValue();
    }

    @Override
    public UnitTypeEnum convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : UnitTypeEnum.fromValue(dbData);
    }
}