package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing pull-unit relationship.
 * This object is used to transfer data from a client's request to the service layer.
 */
@Getter
@Setter
public class PullUnitUpdateDTo {

    /**
     * The unique identifier of the pull event.
     * This field is required.
     */
    @NotNull(message = "The pull id is required")
    private Long pullId;

    /**
     * The unique identifier of the unit (character or weapon) obtained.
     * This field is required.
     */
    @NotNull(message = "The unit id is required")
    private Long unitId;
}