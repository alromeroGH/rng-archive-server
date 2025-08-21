package com.alerom.rng.archive.rng_archive_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for retrieving banner-unit relationship data.
 * This object is used to transfer a complete representation of a banner-unit link from the service layer to the client.
 */
@Getter
@AllArgsConstructor
public class BannerUnitResponseDTO {

    /**
     * The unique identifier of the banner-unit relationship.
     */
    private Long id;

    /**
     * The unique identifier of the banner in the relationship.
     */
    private Long bannerId;

    /**
     * The unique identifier of the unit in the relationship.
     */
    private Long unitId;
}