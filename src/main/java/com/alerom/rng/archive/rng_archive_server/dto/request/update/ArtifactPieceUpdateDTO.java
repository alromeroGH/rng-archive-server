package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import com.alerom.rng.archive.rng_archive_server.models.enums.PieceTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing artifact piece.
 * This object is used to transfer data from a client's request to the service layer,
 * allowing for partial updates of an artifact piece's properties.
 */
@Getter
@Setter
public class ArtifactPieceUpdateDTO {
    /**
     * The new type of the artifact piece (e.g., FLOWER, FEATHER).
     */
    @NotNull(message = "The piece type is required")
    private PieceTypeEnum pieceType;

    /**
     * The new name of the artifact piece (e.g., "Flower of Life").
     */
    @NotBlank(message = "The piece name is required")
    private String pieceName;


    /**
     * The new unique identifier of the artifact set to which this piece belongs.
     */
    @NotNull(message = "The set id is required")
    private Long setId;
}