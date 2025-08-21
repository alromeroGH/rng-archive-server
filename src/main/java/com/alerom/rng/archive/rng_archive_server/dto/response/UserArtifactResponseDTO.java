package com.alerom.rng.archive.rng_archive_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for retrieving user-owned artifact data.
 * This object is used to transfer a complete representation of a user's artifact
 * from the service layer to the client.
 */
@Getter
@AllArgsConstructor
public class UserArtifactResponseDTO {

    /**
     * The unique identifier of the user's artifact record.
     */
    private Long id;

    /**
     * The unique identifier of the user who owns this artifact.
     */
    private Long userId;

    /**
     * The unique identifier of the main stat on the artifact.
     */
    private Long mainStatId;

    /**
     * The unique identifier of the artifact piece (e.g., Flower of Life).
     */
    private Long artifactPieceId;
}