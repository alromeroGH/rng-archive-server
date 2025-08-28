package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import com.alerom.rng.archive.rng_archive_server.models.enums.BannerPhaseEnum;
import com.alerom.rng.archive.rng_archive_server.models.enums.BannerTypeEnum;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * DTO for creating a new banner.
 * This object is used to transfer data from a client's request to the service layer.
 */
@Getter
@Setter
public class BannerCreateDTO {

    /**
     * The type of the banner (e.g., LIMITED_CHARACTER, WEAPON, STANDARD).
     * This field is required.
     */
    @NotNull(message = "The banner type is required")
    private BannerTypeEnum bannerType;

    /**
     * The name of the banner (e.g., "Ballad in Goblets").
     * This field is required.
     */
    @NotBlank(message = "The banner name is required")
    private String bannerName;

    /**
     * The version of the game in which the banner was released (e.g., "1.0", "2.1").
     * This field is required.
     */
    @NotBlank(message = "The banner version is required")
    private String bannerVersion;

    /**
     * The phase of the banner within its version (e.g., ONE, TWO).
     * This field is required.
     */
    @NotNull(message = "The banner phase is required")
    private BannerPhaseEnum bannerPhase;

    /**
     * The start date of the banner.
     * This field is required.
     */
    @NotNull(message = "The banner start date is required")
    private Date bannerStartDate;

    /**
     * A URL or file path to the image representing the banner.
     * This field is required.
     */
    @NotBlank(message = "The banner image is required")
    @Column(unique = true)
    private String bannerImage;
}