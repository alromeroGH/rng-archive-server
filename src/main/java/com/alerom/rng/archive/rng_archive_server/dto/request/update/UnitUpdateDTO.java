package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum;
import com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum;
import com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing unit (character or weapon).
 * This object is used to transfer data from a client's request to the service layer,
 * allowing for partial updates of a unit's properties.
 */
@Getter
@Setter
public class UnitUpdateDTO {

    /**
     * The new type of the unit (e.g., CHARACTER, WEAPON).
     * This field is required.
     */
    @NotNull(message = "The unit type is required")
    private UnitTypeEnum unitType;

    /**
     * The new name of the unit (e.g., "Ganyu", "Primordial Jade Cutter").
     * This field is required.
     */
    @NotBlank(message = "The unit name is required")
    private String unitName;

    /**
     * The new rarity of the unit, represented by the number of stars (e.g., 3, 4, 5).
     * This field is required.
     */
    @NotNull(message = "The number of stars is required")
    private NumberOfStarsEnum numberOfStars;

    /**
     * The new type of banner on which the unit is available (e.g., ALL, CHARACTER, WEAPON).
     * This field is required.
     */
    @NotNull(message = "The unit banner is required")
    private UnitBannerEnum unitBanner;

    /**
     * The new URL or file path to the image representing the unit.
     * This field is required.
     */
    @NotBlank(message = "The unit image is required")
    private String unitImage;
}