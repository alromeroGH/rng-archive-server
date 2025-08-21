package com.alerom.rng.archive.rng_archive_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for retrieving pull-unit relationship data.
 * This object is used to transfer a complete representation of a unit obtained in a pull event,
 * including its relationship to the specific pull and the unit itself.
 */
@Getter
@AllArgsConstructor
public class PullUnitResponseDTO {

    /**
     * The unique identifier of the pull-unit relationship.
     */
    private Long id;

    /**
     * The unique identifier of the pull event to which this unit belongs.
     */
    private Long pullId;

    /**
     * The unique identifier of the unit (character or weapon) obtained in the pull.
     */
    private Long unitId;
}