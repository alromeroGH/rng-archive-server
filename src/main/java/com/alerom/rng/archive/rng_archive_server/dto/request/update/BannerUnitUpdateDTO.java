package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing banner-unit relationship.
 * This object is used to transfer data from a client's request to the service layer.
 */
@Getter
@Setter
public class BannerUnitUpdateDTO {

    /**
     * The identifier of the banner.
     * This field is required.
     */
    @NotNull(message = "The banner id is required")
    private BannerUpdateDTO bannerUpdateDTO;

    /**
     * The identifier of the unit (character or weapon).
     * This field is required.
     */
    @NotNull(message = "The unit id is required")
    private UnitUpdateDTO unitUpdateDTO;
}