package com.alerom.rng.archive.rng_archive_server.models.enums.converters;

import com.alerom.rng.archive.rng_archive_server.models.enums.NewsTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NewsTypeConverter implements AttributeConverter<NewsTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(NewsTypeEnum attribute) {
        return (attribute == null) ? null : attribute.getValue();
    }

    @Override
    public NewsTypeEnum convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : NewsTypeEnum.fromValue(dbData);
    }
}