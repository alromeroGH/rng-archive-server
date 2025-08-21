package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing user-owned artifact.
 * This object is used to transfer data from a client's request to the service layer,
 * allowing for updates to a user-owned artifact's main stat or piece.
 */
@Getter
@Setter
public class UserArtifactUpdateDTO {

    /**
     * The new unique identifier of the main stat for the artifact.
     * This field is required.
     */
    @NotNull(message = "The main stat id is required")
    private Long mainStatId;

    /**
     * The new unique identifier of the artifact piece (e.g., Flower of Life).
     * This field is required.
     */
    @NotNull(message = "The artifact piece id is required")
    private Long artifactPieceId;
}