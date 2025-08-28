package com.alerom.rng.archive.rng_archive_server.models.enums.converters;

import com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UnitBannerConverter implements AttributeConverter<UnitBannerEnum, String> {

    @Override
    public String convertToDatabaseColumn(UnitBannerEnum attribute) {
        return (attribute == null) ? null : attribute.getValue();
    }

    @Override
    public UnitBannerEnum convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : UnitBannerEnum.fromValue(dbData);
    }
}