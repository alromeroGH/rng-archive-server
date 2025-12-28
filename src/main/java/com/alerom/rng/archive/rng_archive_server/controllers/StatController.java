package com.alerom.rng.archive.rng_archive_server.controllers;

import com.alerom.rng.archive.rng_archive_server.dto.response.StatResponseDTO;
import com.alerom.rng.archive.rng_archive_server.services.StatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for managing artifact and unit statistics.
 * It provides endpoints to retrieve the available stats in the system.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/stat")
public class StatController {
    private final StatService statService;

    /**
     * Constructs the StatController with the necessary service.
     * @param statService The service for managing stat information.
     */
    public StatController(StatService statService) {
        this.statService = statService;
    }

    /**
     * Retrieves a list of all statistics available in the database.
     *
     * @return A ResponseEntity containing a list of stat response DTOs.
     */
    @GetMapping
    public ResponseEntity<?> listStats() {
        List<StatResponseDTO> statResponseDTOS = statService.listStats();

        return ResponseEntity.ok(statResponseDTOS);
    }
}
