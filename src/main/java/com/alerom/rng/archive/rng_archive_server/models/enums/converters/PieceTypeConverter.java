package com.alerom.rng.archive.rng_archive_server.models.enums.converters;

import com.alerom.rng.archive.rng_archive_server.models.enums.PieceTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PieceTypeConverter implements AttributeConverter<PieceTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(PieceTypeEnum attribute) {
        return (attribute == null) ? null : attribute.getValue();
    }

    @Override
    public PieceTypeEnum convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : PieceTypeEnum.fromValue(dbData);
    }
}