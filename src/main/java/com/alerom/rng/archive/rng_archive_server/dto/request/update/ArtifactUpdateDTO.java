package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArtifactUpdateDTO {

    @NotNull(message = "The artifact set is required")
    private ArtifactSetUpdateDTO artifactSet;

    @NotNull(message = "The artifact piece ids are required")
    private List<ArtifactPieceUpdateDTO> artifactPieces;
}