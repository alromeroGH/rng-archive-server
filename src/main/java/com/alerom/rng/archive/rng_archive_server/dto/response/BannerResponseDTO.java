package com.alerom.rng.archive.rng_archive_server.dto.response;

import com.alerom.rng.archive.rng_archive_server.models.enums.BannerPhaseEnum;
import com.alerom.rng.archive.rng_archive_server.models.enums.BannerTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * DTO for retrieving banner data.
 * This object is used to transfer a complete representation of a banner from the service layer to the client.
 */
@Getter
@AllArgsConstructor
public class BannerResponseDTO {

    /**
     * The unique identifier of the banner.
     */
    private Long id;

    /**
     * The type of the banner (e.g., LIMITED_CHARACTER, WEAPON, STANDARD).
     */
    private BannerTypeEnum bannerType;

    /**
     * The name of the banner.
     */
    private String bannerName;

    /**
     * The version of the game in which the banner was released.
     */
    private String bannerVersion;

    /**
     * The phase of the banner within its version (e.g., ONE, TWO).
     */
    private BannerPhaseEnum bannerPhase;

    /**
     * The start date of the banner.
     */
    private Date bannerStartDate;

    /**
     * A URL or file path to the image representing the banner.
     */
    private String bannerImage;
}