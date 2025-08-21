package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new banner-unit relationship.
 * This object is used to link a specific unit to a banner.
 */
@Getter
@Setter
public class BannerUnitCreateDTO {

    /**
     * The unique identifier of the banner.
     * This field is required.
     */
    @NotNull(message = "The banner id is required")
    private Long bannerId;

    /**
     * The unique identifier of the unit (character or weapon).
     * This field is required.
     */
    @NotNull(message = "The unit id is required")
    private Long unitId;
}