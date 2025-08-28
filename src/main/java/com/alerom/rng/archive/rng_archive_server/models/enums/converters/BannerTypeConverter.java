package com.alerom.rng.archive.rng_archive_server.models.enums.converters;

import com.alerom.rng.archive.rng_archive_server.models.enums.BannerTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BannerTypeConverter implements AttributeConverter<BannerTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(BannerTypeEnum attribute) {
        return (attribute == null) ? null : attribute.getValue();
    }

    @Override
    public BannerTypeEnum convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : BannerTypeEnum.fromValue(dbData);
    }
}