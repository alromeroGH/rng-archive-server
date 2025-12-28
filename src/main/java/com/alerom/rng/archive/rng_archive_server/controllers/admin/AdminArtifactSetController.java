package com.alerom.rng.archive.rng_archive_server.controllers.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.ArtifactCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.ArtifactUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.ArtifactResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.ArtifactNotFoundException;
import com.alerom.rng.archive.rng_archive_server.exceptions.InvalidImageException;
import com.alerom.rng.archive.rng_archive_server.exceptions.LimitException;
import com.alerom.rng.archive.rng_archive_server.services.admin.AdminArtifactSetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for administrative management of artifact sets.
 * It provides restricted endpoints to create, list, update, and delete artifact set configurations.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/artifactSet")
public class AdminArtifactSetController {
    private final AdminArtifactSetService adminArtifactSetService;

    /**
     * Constructs the AdminArtifactSetController with the necessary service.
     * @param adminArtifactSetService The service for administrative artifact set operations.
     */
    public AdminArtifactSetController(AdminArtifactSetService adminArtifactSetService) {
        this.adminArtifactSetService = adminArtifactSetService;
    }

    /**
     * Creates a new artifact set.
     *
     * @param artifactCreateDTO The DTO containing the information to create the artifact set.
     * @return A ResponseEntity containing the created artifact set data or an error message if the image is invalid or limits are exceeded.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createArtifactSet(@Valid @RequestBody ArtifactCreateDTO artifactCreateDTO) {
        try {
            ArtifactResponseDTO artifactResponseDTO = adminArtifactSetService.createArtifactSet(artifactCreateDTO);

            return ResponseEntity.ok(artifactResponseDTO);
        } catch (InvalidImageException | LimitException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * Retrieves a list of all artifact sets available in the system.
     *
     * @return A ResponseEntity containing a list of artifact set response DTOs.
     */
    @GetMapping
    public ResponseEntity<?> listArtifactSets() {
        List<ArtifactResponseDTO> artifactResponseDTO = adminArtifactSetService.listArtifactSets();

        return ResponseEntity.ok(artifactResponseDTO);
    }

    /**
     * Updates an existing artifact set by its unique ID.
     *
     * @param id The ID of the artifact set to update.
     * @param artifactUpdateDTO The DTO containing the updated artifact set information.
     * @return A ResponseEntity containing the updated artifact set data or an error message if not found or validation fails.
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateArtifactSet(@PathVariable Long id, @Valid @RequestBody ArtifactUpdateDTO artifactUpdateDTO) {
        try {
            ArtifactResponseDTO artifactResponseDTO = adminArtifactSetService.updateArtifactSet(id, artifactUpdateDTO);

            return ResponseEntity.ok(artifactResponseDTO);
        } catch (ArtifactNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (InvalidImageException | LimitException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * Deletes a specific artifact set by its unique ID.
     *
     * @param id The ID of the artifact set to delete.
     * @return A ResponseEntity containing the deleted artifact set data or an error message if not found.
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteArtifactSet(@PathVariable Long id) {
        try {
            ArtifactResponseDTO artifactResponseDTO = adminArtifactSetService.deleteArtifactSet(id);

            return ResponseEntity.ok(artifactResponseDTO);
        } catch (ArtifactNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        }
    }
}