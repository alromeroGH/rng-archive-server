package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArtifactCreateDTO {

    @NotNull(message = "The artifact set is required")
    private ArtifactSetCreateDTO artifactSet;

    @NotNull(message = "The artifact pieces are required")
    private List<ArtifactPieceCreateDTO> artifactPieces;
}