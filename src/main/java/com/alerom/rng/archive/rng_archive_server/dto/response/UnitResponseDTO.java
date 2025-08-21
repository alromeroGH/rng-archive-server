package com.alerom.rng.archive.rng_archive_server.dto.response;

import com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum;
import com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum;
import com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for retrieving unit data.
 * This object is used to transfer a complete representation of a unit (character or weapon)
 * from the service layer to the client.
 */
@Getter
@AllArgsConstructor
public class UnitResponseDTO {

    /**
     * The unique identifier of the unit.
     */
    private Long id;

    /**
     * The type of the unit (e.g., CHARACTER, WEAPON).
     */
    private UnitTypeEnum unitType;

    /**
     * The name of the unit (e.g., "Ganyu", "Primordial Jade Cutter").
     */
    private String unitName;

    /**
     * The rarity of the unit, represented by the number of stars (e.g., 3, 4, 5).
     */
    private NumberOfStarsEnum numberOfStars;

    /**
     * The type of banner on which the unit is available (e.g., ALL, CHARACTER, WEAPON).
     */
    private UnitBannerEnum unitBanner;

    /**
     * A URL or file path to the image representing the unit.
     */
    private String unitImage;
}