package com.alerom.rng.archive.rng_archive_server.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents the phase of a banner's release within a specific game version.
 * Each version can have multiple banner phases.
 */
public enum BannerPhaseEnum {
    /**
     * The first phase of a banner release.
     */
    ONE("1"),

    /**
     * The second phase of a banner release.
     */
    TWO("2"),

    /**
     * Used for banners that do not follow a phase system, such as standard banners.
     */
    NO_PHASE("-");

    private final String value;

    BannerPhaseEnum(String value) {
        this.value = value;
    }

    @JsonCreator
    public static BannerPhaseEnum fromValue(String value) {
        for (BannerPhaseEnum phase : BannerPhaseEnum.values()) {
            if (phase.getValue().equals(value)) {
                return phase;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }

    /**
     * Returns the string representation of the banner phase.
     * @return The string value of the enum constant.
     */
    public String getValue(){
        return value;
    }
}