package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new pull-unit relationship.
 * This object is used to link a specific unit obtained in a pull event.
 */
@Getter
@Setter
public class PullUnitCreateDTO {

    /**
     * The unique identifier of the pull event.
     * This field is required.
     */
    @NotNull(message = "The pull id is required")
    private Long pullId;

    /**
     * The unique identifier of the unit obtained.
     * This field is required.
     */
    @NotNull(message = "The unit id is required")
    private Long unitId;
}