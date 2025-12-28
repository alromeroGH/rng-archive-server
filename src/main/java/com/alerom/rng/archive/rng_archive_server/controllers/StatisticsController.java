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

/**
 * Controller class for managing statistical analysis operations.
 * It provides endpoints to retrieve statistics and predictions for artifacts and pull results.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final ArtifactStatisticService artifactStatisticService;
    private final PullStatisticService pullStatisticService;

    /**
     * Constructs the StatisticsController with the necessary services.
     * @param artifactStatisticService The service for artifact statistical calculations.
     * @param pullStatisticService The service for pull/summon statistical calculations.
     */
    public StatisticsController(ArtifactStatisticService artifactStatisticService, PullStatisticService pullStatisticService) {
        this.artifactStatisticService = artifactStatisticService;
        this.pullStatisticService = pullStatisticService;
    }

    /**
     * Retrieves statistics and predictions for a specific artifact configuration.
     *
     * @param artifactPredictionCreateDTO The DTO containing the parameters for artifact prediction.
     * @return A ResponseEntity containing the artifact statistics or an error message if stats are not found or data is insufficient.
     */
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

    /**
     * Retrieves statistics based on pull/summon history and parameters.
     *
     * @param pullStatisticCreateDTO The DTO containing the parameters for pull analysis.
     * @return A ResponseEntity containing the pull statistics or an error message if there is insufficient data to perform the analysis.
     */
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