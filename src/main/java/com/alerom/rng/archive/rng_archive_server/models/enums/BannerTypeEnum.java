package com.alerom.rng.archive.rng_archive_server.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the type of a banner in the game.
 * This enum differentiates between different banner types, each with its own specific rules.
 */
public enum BannerTypeEnum {
    /**
     * A limited-time banner featuring a specific character.
     */
    LIMITED_CHARACTER("limited_character"),

    /**
     * A limited-time banner featuring specific weapons.
     */
    WEAPON("weapon"),

    /**
     * The permanent, non-limited banner.
     */
    STANDARD("standard");

    private final String value;

    BannerTypeEnum(String value) {
        this.value = value;
    }

    @JsonCreator
    public static BannerTypeEnum fromValue(String value) {
        for (BannerTypeEnum bannerType : BannerTypeEnum.values()) {
            if (bannerType.getValue().equals(value)) {
                return bannerType;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }

    /**
     * Returns the string representation of the banner type.
     * @return The string value of the enum constant.
     */
    public String getValue(){
        return value;
    }
}