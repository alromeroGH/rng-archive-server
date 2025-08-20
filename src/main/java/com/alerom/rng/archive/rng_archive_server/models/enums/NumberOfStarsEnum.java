package com.alerom.rng.archive.rng_archive_server.models.enums;

/**
 * Represents the rarity of a unit based on a star rating.
 */
public enum NumberOfStarsEnum {
    /**
     * A unit with 3-star rarity.
     */
    THREE_STARS("3"),

    /**
     * A unit with 4-star rarity.
     */
    FOUR_STARS("4"),

    /**
     * A unit with 5-star rarity, the highest rarity.
     */
    FIVE_STARS("5");

    private final String value;

    NumberOfStarsEnum(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the star rating.
     * @return The string value of the enum constant.
     */
    public String getValue(){
        return value;
    }
}