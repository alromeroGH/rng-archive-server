package com.alerom.rng.archive.rng_archive_server.controllers;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.ArtifactPredictionCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.create.PullStatisticCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.ArtifactStatisticsResponseDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.PullStatisticResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.InsufficientDataException;
import com.alerom.rng.archive.rng_archive_server.exceptions.StatNotFoundException;
import com.alerom.rng.archive.rng_archive_server.services.ArtifactStatisticService;
import com.alerom.rng.archive.rng_archive_server.services.PullStatisticService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final ArtifactStatisticService artifactStatisticService;
    private final PullStatisticService pullStatisticService;

    public StatisticsController(ArtifactStatisticService artifactStatisticService, PullStatisticService pullStatisticService) {
        this.artifactStatisticService = artifactStatisticService;
        this.pullStatisticService = pullStatisticService;
    }

    @PostMapping("/artifact")
    public ResponseEntity<?> getArtifactStatistics(@Valid @RequestBody ArtifactPredictionCreateDTO artifactPredictionCreateDTO) {
        try {
            ArtifactStatisticsResponseDTO artifactStatisticsResponseDTO = artifactStatisticService.getArtifactStatistics(artifactPredictionCreateDTO);

            return ResponseEntity.ok(artifactStatisticsResponseDTO);
        } catch (StatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (InsufficientDataException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/pull")
    public ResponseEntity<?> getPullStatistics(@Valid @RequestBody PullStatisticCreateDTO pullStatisticCreateDTO) {
        try {
            PullStatisticResponseDTO pullStatisticResponseDTO = pullStatisticService.getPullStatistics(pullStatisticCreateDTO);

            return ResponseEntity.ok(pullStatisticResponseDTO);
        } catch (InsufficientDataException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }
}