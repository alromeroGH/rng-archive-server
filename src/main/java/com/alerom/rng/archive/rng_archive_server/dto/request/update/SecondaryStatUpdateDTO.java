package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing secondary stat record for a user's artifact.
 * This object is used to transfer data from a client's request to the service layer.
 */
@Getter
@Setter
public class SecondaryStatUpdateDTO {

    /**
     * The unique identifier of the stat (e.g., ATK%, CRIT DMG).
     * This field is required.
     */
    @NotNull(message = "The stat id is required")
    private Long statId;

    /**
     * The unique identifier of the user's artifact to which this secondary stat belongs.
     * This field is required.
     */
    @NotNull(message = "The user artifact id is required")
    private Long userArtifactId;
}