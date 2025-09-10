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

@RestController
@RequestMapping("/api/userArtifact")
public class UserArtifactController {
    private final UserArtifactService userArtifactService;

    public UserArtifactController(UserArtifactService userArtifactService) {
        this.userArtifactService = userArtifactService;
    }

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

    @GetMapping
    public ResponseEntity<?> listUserArtifacts() {
        List<UserArtifactResponseDTO> userArtifactResponseDTOS = userArtifactService.listUserArtifacts();

        return ResponseEntity.ok(userArtifactResponseDTOS);
    }

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