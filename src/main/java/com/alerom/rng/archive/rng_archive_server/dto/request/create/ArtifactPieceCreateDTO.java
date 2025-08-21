package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import com.alerom.rng.archive.rng_archive_server.models.enums.PieceTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new artifact piece.
 * This object is used to transfer data from a client's request to the service layer.
 */
@Getter
@Setter
public class ArtifactPieceCreateDTO {

    /**
     * The type of the artifact piece (e.g., FLOWER, FEATHER).
     * This field is required.
     */
    @NotNull(message = "The piece type is required")
    private PieceTypeEnum pieceType;

    /**
     * The name of the artifact piece (e.g., "Flower of Life").
     * This field is required.
     */
    @NotBlank(message = "The piece name is required")
    private String pieceName;

    /**
     * The unique identifier of the artifact set to which this piece belongs.
     * This field is required.
     */
    @NotNull(message = "The set id is required")
    private Long setId;
}