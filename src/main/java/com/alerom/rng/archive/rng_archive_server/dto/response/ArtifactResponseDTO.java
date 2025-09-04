package com.alerom.rng.archive.rng_archive_server.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ArtifactResponseDTO {

    @NotBlank(message = "The artifact set is required")
    private ArtifactSetResponseDTO artifactSet;

    @NotBlank(message = "The artifact pieces are required")
    private List<ArtifactPieceResponseDTO> artifactPieces;
}