package com.alerom.rng.archive.rng_archive_server.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the type of a stat, indicating whether it can be an only-main stat or both.
 */
public enum StatTypeEnum {
    /**
     * A stat that can only appear as a main stat on an artifact.
     */
    MAIN("main_only"),

    /**
     * A stat that can appear as both a main stat and a secondary stat.
     */
    BOTH("both");

    private final String value;

    StatTypeEnum(String value) {
        this.value = value;
    }

    @JsonCreator
    public static StatTypeEnum fromValue(String value) {
        for (StatTypeEnum statType : StatTypeEnum.values()) {
            if (statType.getValue().equals(value)) {
                return statType;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }

    /**
     * Returns the string representation of the stat type.
     * @return The string value of the enum constant.
     */
    public String getValue(){
        return value;
    }
}