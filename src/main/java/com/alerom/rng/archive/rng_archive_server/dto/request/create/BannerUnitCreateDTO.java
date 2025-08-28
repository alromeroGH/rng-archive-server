package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import com.alerom.rng.archive.rng_archive_server.dto.response.BannerResponseDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.UnitResponseDTO;
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
     * The identifier of the banner.
     * This field is required.
     */
    @NotNull(message = "The banner id is required")
    private BannerResponseDTO banner;

    /**
     * The identifier of the unit (character or weapon).
     * This field is required.
     */
    @NotNull(message = "The unit id is required")
    private UnitResponseDTO unit;
}