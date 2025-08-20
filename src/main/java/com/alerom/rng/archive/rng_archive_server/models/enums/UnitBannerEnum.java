package com.alerom.rng.archive.rng_archive_server.models.enums;

/**
 * Represents the type of banner on which a unit is typically featured.
 * This enum helps to categorize units based on their availability in different banners.
 */
public enum UnitBannerEnum {
    /**
     * A unit that can be obtained from all types of banners (e.g., Standard and Limited).
     */
    ALL("all"),

    /**
     * A unit that is specifically featured on a character banner.
     */
    CHARACTER("character"),

    /**
     * A unit that is specifically featured on a weapon banner.
     */
    WEAPON("weapon");

    private final String value;

    UnitBannerEnum(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the unit banner type.
     * @return The string value of the enum constant.
     */
    public String getValue(){
        return value;
    }
}