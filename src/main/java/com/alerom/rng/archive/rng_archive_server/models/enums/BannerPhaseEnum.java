package com.alerom.rng.archive.rng_archive_server.models.enums;

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

    /**
     * Returns the string representation of the banner phase.
     * @return The string value of the enum constant.
     */
    public String getValue(){
        return value;
    }
}