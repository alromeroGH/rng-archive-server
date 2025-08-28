package com.alerom.rng.archive.rng_archive_server.models.enums.converters;

import com.alerom.rng.archive.rng_archive_server.models.enums.BannerPhaseEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BannerPhaseEnumConverter implements AttributeConverter<BannerPhaseEnum, String> {

    @Override
    public String convertToDatabaseColumn(BannerPhaseEnum attribute) {
        return (attribute == null) ? null : attribute.getValue();
    }

    @Override
    public BannerPhaseEnum convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : BannerPhaseEnum.fromValue(dbData);
    }
}