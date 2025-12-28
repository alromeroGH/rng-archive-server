package com.alerom.rng.archive.rng_archive_server.controllers.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.WeaponBannerCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.WeaponBannerUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.WeaponBannerResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.*;
import com.alerom.rng.archive.rng_archive_server.services.admin.AdminWeaponBannerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for administrative management of weapon banners.
 * It provides restricted endpoints to create, list, update, and delete weapon banner configurations.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/weaponBanner")
public class AdminWeaponBannerController {
    private final AdminWeaponBannerService adminWeaponBannerService;

    /**
     * Constructs the AdminWeaponBannerController with the necessary service.
     * @param adminWeaponBannerService The service for administrative weapon banner operations.
     */
    public AdminWeaponBannerController(AdminWeaponBannerService adminWeaponBannerService) {
        this.adminWeaponBannerService = adminWeaponBannerService;
    }

    /**
     * Creates a new weapon banner configuration.
     *
     * @param weaponBannerCreateDTO The DTO containing the details for the new weapon banner.
     * @return A ResponseEntity containing the created weapon banner data or an error message if entities are not found or validation fails.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createWeaponBanner (@Valid @RequestBody WeaponBannerCreateDTO weaponBannerCreateDTO) {
        try {
            WeaponBannerResponseDTO weaponBannerResponseDTO = adminWeaponBannerService.createWeaponBanner(weaponBannerCreateDTO);

            return ResponseEntity.ok(weaponBannerResponseDTO);
        } catch (BannerNotFoundException | UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (LimitException | InvalidUnitException | InvalidImageException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * Retrieves a list of all existing weapon banners.
     *
     * @return A ResponseEntity containing a list of weapon banner response DTOs.
     */
    @GetMapping
    public ResponseEntity<?> listCharacterBanner() {
        try {
            List<WeaponBannerResponseDTO> weaponBannerResponseDTOS = adminWeaponBannerService.listWeaponBanner();

            return ResponseEntity.ok(weaponBannerResponseDTOS);
        } catch (LimitException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
        }
    }

    /**
     * Updates an existing weapon banner configuration by its unique ID.
     *
     * @param id The ID of the weapon banner to update.
     * @param weaponBannerUpdateDTO The DTO containing the updated banner information.
     * @return A ResponseEntity containing the updated weapon banner data or an error message if not found or validation fails.
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCharacterBanner(@PathVariable Long id, @Valid @RequestBody WeaponBannerUpdateDTO weaponBannerUpdateDTO) {
        try {
            WeaponBannerResponseDTO weaponBannerResponseDTO = adminWeaponBannerService.updateWeaponBanner(id, weaponBannerUpdateDTO);

            return ResponseEntity.ok(weaponBannerResponseDTO);
        } catch (BannerNotFoundException | UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (LimitException | InvalidUnitException | InvalidImageException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * Deletes a specific weapon banner configuration by its unique ID.
     *
     * @param id The ID of the weapon banner to delete.
     * @return A ResponseEntity containing the deleted weapon banner data or an error message if not found or deletion is restricted.
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCharacterBanner(@PathVariable Long id) {
        try {
            WeaponBannerResponseDTO weaponBannerResponseDTO = adminWeaponBannerService.deleteWeaponBanner(id);

            return ResponseEntity.ok(weaponBannerResponseDTO);
        } catch (BannerNotFoundException | UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (LimitException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }
}