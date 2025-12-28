package com.alerom.rng.archive.rng_archive_server.controllers;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.UserArtifactCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.UserArtifactUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.UserArtifactResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.*;
import com.alerom.rng.archive.rng_archive_server.services.UserArtifactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing user artifact operations.
 * It provides endpoints to create, retrieve, update, and delete artifacts associated with users.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/userArtifact")
public class UserArtifactController {
    private final UserArtifactService userArtifactService;

    /**
     * Constructs the UserArtifactController with the necessary service.
     * @param userArtifactService The service for managing user artifacts.
     */
    public UserArtifactController(UserArtifactService userArtifactService) {
        this.userArtifactService = userArtifactService;
    }

    /**
     * Creates a new user artifact.
     *
     * @param userArtifactCreateDTO The DTO containing the information to create the artifact.
     * @return A ResponseEntity containing the created artifact data or an error message if related entities are not found or limits are exceeded.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createUserArtifact(@Valid @RequestBody UserArtifactCreateDTO userArtifactCreateDTO) {
        try {
            UserArtifactResponseDTO userArtifactResponseDTO = userArtifactService.createUserArtifact(userArtifactCreateDTO);

            return ResponseEntity.ok(userArtifactResponseDTO);
        } catch (UserNotFoundException | StatNotFoundException | ArtifactNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (LimitException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * Retrieves a list of all artifacts associated with the current user context.
     *
     * @return A ResponseEntity containing a list of user artifact response DTOs.
     */
    @GetMapping
    public ResponseEntity<?> listUserArtifacts() {
        List<UserArtifactResponseDTO> userArtifactResponseDTOS = userArtifactService.listUserArtifacts();

        return ResponseEntity.ok(userArtifactResponseDTOS);
    }

    /**
     * Updates an existing user artifact by its unique ID.
     *
     * @param id The ID of the artifact to update.
     * @param userArtifactUpdateDTO The DTO containing the updated information.
     * @return A ResponseEntity containing the updated artifact data or an error message if the artifact/entities are not found or limits are exceeded.
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateUserArtifact(@PathVariable Long id, @Valid @RequestBody UserArtifactUpdateDTO userArtifactUpdateDTO) {
        try {
            UserArtifactResponseDTO userArtifactResponseDTO = userArtifactService.updateUserArtifact(id, userArtifactUpdateDTO);

            return ResponseEntity.ok(userArtifactResponseDTO);
        } catch (UserArtifactNotFoundException | StatNotFoundException | ArtifactNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (LimitException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * Deletes a user artifact by its unique ID.
     *
     * @param id The ID of the artifact to delete.
     * @return A ResponseEntity containing the data of the deleted artifact or an error message if not found.
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserArtifact(@PathVariable Long id) {
        try {
            UserArtifactResponseDTO userArtifactResponseDTO = userArtifactService.deleteUserArtifact(id);

            return ResponseEntity.ok(userArtifactResponseDTO);
        } catch (UserArtifactNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}