package com.alerom.rng.archive.rng_archive_server.controllers.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.UnitCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.UnitUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.UnitResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.InvalidImageException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UnitNotFoundException;
import com.alerom.rng.archive.rng_archive_server.services.admin.AdminUnitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/unit")
public class AdminUnitController {
    private final AdminUnitService adminUnitService;

    public AdminUnitController(AdminUnitService adminUnitService) {
        this.adminUnitService = adminUnitService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUnit(@Valid @RequestBody UnitCreateDTO unitCreateDTO) {
        try {
            UnitResponseDTO unitResponseDTO = adminUnitService.createUnit(unitCreateDTO);

            return ResponseEntity.ok(unitCreateDTO);
        } catch (InvalidImageException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listUnit() {
        List<UnitResponseDTO> unitResponseDTOS = adminUnitService.listUnits();

        return ResponseEntity.ok(unitResponseDTOS);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateUnit(@PathVariable Long id, @Valid @RequestBody UnitUpdateDTO unitUpdateDTO) {
        try {
            UnitResponseDTO unitResponseDTO = adminUnitService.updateUnit(id, unitUpdateDTO);

            return ResponseEntity.ok(unitResponseDTO);
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (InvalidImageException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteUnit(@PathVariable Long id) {
        try {
            UnitResponseDTO unitResponseDTO = adminUnitService.deleteUnit(id);

            return ResponseEntity.ok(unitResponseDTO);
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}