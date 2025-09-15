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

@RestController
@RequestMapping("/api/pull")
public class PullController {
    private final PullService pullService;

    public PullController(PullService pullService) {
        this.pullService = pullService;
    }

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

    @GetMapping
    public ResponseEntity<?> listPulls() {
        List<PullResponseDTO> pullResponseDTOS = pullService.listPulls();

        return ResponseEntity.ok(pullResponseDTOS);
    }

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