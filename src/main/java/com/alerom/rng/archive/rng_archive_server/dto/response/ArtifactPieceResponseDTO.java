package com.alerom.rng.archive.rng_archive_server.dto.response;

import com.alerom.rng.archive.rng_archive_server.models.enums.PieceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for retrieving artifact piece data.
 * This object is used to transfer a complete representation of an artifact piece from the service layer to the client.
 */
@Getter
@AllArgsConstructor
public class ArtifactPieceResponseDTO {

    /**
     * The unique identifier of the artifact piece.
     */
    private Long id;

    /**
     * The type of the artifact piece (e.g., FLOWER, FEATHER).
     */
    private PieceTypeEnum pieceType;

    /**
     * The name of the artifact piece.
     */
    private String pieceName;
}