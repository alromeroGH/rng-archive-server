package com.alerom.rng.archive.rng_archive_server.models.enums;

/**
 * Represents the type of a unit in the game.
 * A unit can be either a playable character or a weapon.
 */
public enum UnitTypeEnum {
    /**
     * A playable character unit.
     */
    CHARACTER("character"),

    /**
     * A weapon unit.
     */
    WEAPON("weapon");

    private final String value;

    UnitTypeEnum(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the unit type.
     * @return The string value of the enum constant.
     */
    public String getValue(){
        return value;
    }
}