package com.alerom.rng.archive.rng_archive_server.models.enums;

/**
 * Represents the type of an artifact piece based on its slot.
 * Each artifact set has five different piece types.
 */
public enum PieceTypeEnum {
    /**
     * The flower-type artifact piece.
     */
    FLOWER("flower"),

    /**
     * The feather-type artifact piece.
     */
    FEATHER("feather"),

    /**
     * The sands-type artifact piece.
     */
    SANDS("sands"),

    /**
     * The goblet-type artifact piece.
     */
    GOBLET("goblet"),

    /**
     * The circlet-type artifact piece.
     */
    CIRCLET("circlet");

    private final String value;

    PieceTypeEnum(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the artifact piece type.
     * @return The string value of the enum constant.
     */
    public String getValue(){
        return value;
    }
}