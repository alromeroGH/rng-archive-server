package com.alerom.rng.archive.rng_archive_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for retrieving secondary stat data.
 * This object is used to transfer a complete representation of a secondary stat
 * on a user-owned artifact from the service layer to the client.
 */
@Getter
@AllArgsConstructor
public class SecondaryStatResponseDTO {

    /**
     * The unique identifier of the secondary stat record.
     */
    private Long id;

    /**
     * The unique identifier of the stat itself (e.g., ATK%, CRIT DMG).
     */
    private StatResponseDTO stat;
}