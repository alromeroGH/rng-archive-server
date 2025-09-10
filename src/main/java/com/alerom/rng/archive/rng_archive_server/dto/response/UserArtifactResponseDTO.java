package com.alerom.rng.archive.rng_archive_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

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
    private UserResponseDTO user;

    /**
     * The unique identifier of the main stat on the artifact.
     */
    private StatResponseDTO mainStat;

    /**
     * The unique identifier of the artifact piece (e.g., Flower of Life).
     */
    private ArtifactPieceResponseDTO artifactPiece;

    private List<SecondaryStatResponseDTO> secondaryStats;
}