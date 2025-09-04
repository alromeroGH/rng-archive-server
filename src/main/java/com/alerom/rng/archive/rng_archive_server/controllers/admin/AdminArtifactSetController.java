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

@RestController
@RequestMapping("/api/admin/artifactSet")
public class AdminArtifactSetController {
    private final AdminArtifactSetService adminArtifactSetService;

    public AdminArtifactSetController(AdminArtifactSetService adminArtifactSetService) {
        this.adminArtifactSetService = adminArtifactSetService;
    }

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

    @GetMapping
    public ResponseEntity<?> listArtifactSets() {
        List<ArtifactResponseDTO> artifactResponseDTO = adminArtifactSetService.listArtifactSets();

        return ResponseEntity.ok(artifactResponseDTO);
    }

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