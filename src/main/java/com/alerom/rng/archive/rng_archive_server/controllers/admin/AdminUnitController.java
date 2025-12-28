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

/**
 * Controller class for administrative management of units (characters or weapons).
 * It provides restricted endpoints to create, list, update, and delete unit data.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/unit")
public class AdminUnitController {
    private final AdminUnitService adminUnitService;

    /**
     * Constructs the AdminUnitController with the necessary service.
     * @param adminUnitService The service for administrative unit operations.
     */
    public AdminUnitController(AdminUnitService adminUnitService) {
        this.adminUnitService = adminUnitService;
    }

    /**
     * Creates a new unit in the system.
     *
     * @param unitCreateDTO The DTO containing the details for the new unit.
     * @return A ResponseEntity containing the created unit data or an error message if the image is invalid.
     */
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

    /**
     * Retrieves a list of all units registered in the system.
     *
     * @return A ResponseEntity containing a list of unit response DTOs.
     */
    @GetMapping
    public ResponseEntity<?> listUnit() {
        List<UnitResponseDTO> unitResponseDTOS = adminUnitService.listUnits();

        return ResponseEntity.ok(unitResponseDTOS);
    }

    /**
     * Updates an existing unit's information by its unique ID.
     *
     * @param id The ID of the unit to update.
     * @param unitUpdateDTO The DTO containing the updated unit information.
     * @return A ResponseEntity containing the updated unit data or an error message if the unit is not found or the image is invalid.
     */
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

    /**
     * Deletes a unit from the system by its unique ID.
     *
     * @param id The ID of the unit to delete.
     * @return A ResponseEntity containing the deleted unit data or an error message if the unit is not found.
     */
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