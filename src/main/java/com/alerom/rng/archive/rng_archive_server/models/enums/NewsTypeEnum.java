package com.alerom.rng.archive.rng_archive_server.models.enums;

/**
 * Represents the type of a news item.
 * This enum helps categorize different kinds of announcements made in the game.
 */
public enum NewsTypeEnum {
    /**
     * News related to special in-game events.
     */
    EVENT("event"),

    /**
     * News related to new or returning banners.
     */
    BANNER("banner"),

    /**
     * News providing redeemable codes for in-game rewards.
     */
    CODE("code");

    private final String value;

    NewsTypeEnum(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the news type.
     * @return The string value of the enum constant.
     */
    public String getValue(){
        return value;
    }
}
