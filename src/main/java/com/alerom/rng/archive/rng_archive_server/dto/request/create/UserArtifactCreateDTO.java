package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO for creating a new user-owned artifact.
 * This object is used to transfer data from a client's request to the service layer,
 * linking a user to a specific artifact piece and its main stat.
 */
@Getter
@Setter
public class UserArtifactCreateDTO {

    private Long userId;

    /**
     * The unique identifier of the main stat for the artifact.
     * This field is required.
     */
    @NotNull(message = "The main stat id is required")
    private Long mainStatId;

    /**
     * The unique identifier of the artifact piece (e.g., Flower of Life).
     * This field is required.
     */
    @NotNull(message = "The artifact piece id is required")
    private Long artifactPieceId;

    private List<Long> secondaryStatIds;
}