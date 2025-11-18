package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArtifactPredictionCreateDTO {
    @NotNull(message = "The userId is required")
    private Long userId;

    @NotNull(message = "The ArtifactSetId is required")
    private Long artifactSetId;

    @NotNull(message = "The artifactPieceId is required")
    private Long artifactPieceId;

    @NotNull(message = "The mainStatId is required")
    private Long mainStatId;

}