package com.alerom.rng.archive.rng_archive_server.controllers;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.PullCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.PullUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.PullResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.BannerNotFoundException;
import com.alerom.rng.archive.rng_archive_server.exceptions.PullNotFoundException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UnitNotFoundException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UserNotFoundException;
import com.alerom.rng.archive.rng_archive_server.services.PullService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing pull (summon) records.
 * It provides endpoints to create, retrieve, update, and delete individual pull entries.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/pull")
public class PullController {
    private final PullService pullService;

    /**
     * Constructs the PullController with the necessary service.
     * @param pullService The service for pull data management.
     */
    public PullController(PullService pullService) {
        this.pullService = pullService;
    }

    /**
     * Registers a new pull entry in the system.
     *
     * @param pullCreateDTO The DTO containing the pull details to be created.
     * @return A ResponseEntity containing the created pull data or an error message if the user, banner, or unit is not found.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createPull(@Valid @RequestBody PullCreateDTO pullCreateDTO) {
        try {
            PullResponseDTO pullResponseDTO = pullService.createPull(pullCreateDTO);

            return ResponseEntity.ok(pullResponseDTO);
        } catch (UserNotFoundException | BannerNotFoundException | UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    /**
     * Retrieves a list of all pulls recorded in the system.
     *
     * @return A ResponseEntity containing a list of pull response DTOs.
     */
    @GetMapping
    public ResponseEntity<?> listPulls() {
        List<PullResponseDTO> pullResponseDTOS = pullService.listPulls();

        return ResponseEntity.ok(pullResponseDTOS);
    }

    /**
     * Updates an existing pull record by its unique ID.
     *
     * @param id The ID of the pull to update.
     * @param pullUpdateDTO The DTO containing the updated pull information.
     * @return A ResponseEntity containing the updated pull data or an error message if the pull or related entities are not found.
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updatePull(@PathVariable Long id, @Valid @RequestBody PullUpdateDTO pullUpdateDTO) {
        try {
            PullResponseDTO pullResponseDTO = pullService.updatePull(id, pullUpdateDTO);

            return ResponseEntity.ok(pullResponseDTO);
        } catch (PullNotFoundException | UserNotFoundException | BannerNotFoundException | UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }


    /**
     * Deletes a specific pull record by its unique ID.
     *
     * @param id The ID of the pull to delete.
     * @return A ResponseEntity containing the deleted pull data or an error message if the pull is not found.
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deletePull(@PathVariable Long id) {
        try {
            PullResponseDTO pullResponseDTO = pullService.deletePull(id);

            return ResponseEntity.ok(pullResponseDTO);
        } catch (PullNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}