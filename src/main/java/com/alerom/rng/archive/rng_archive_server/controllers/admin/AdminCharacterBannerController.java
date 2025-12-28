package com.alerom.rng.archive.rng_archive_server.controllers.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.CharacterBannerCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.CharacterBannerUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.CharacterBannerResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.*;
import com.alerom.rng.archive.rng_archive_server.services.admin.AdminCharacterBannerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for administrative management of character banners.
 * It provides restricted endpoints to create, list, update, and delete character banner configurations.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/characterBanner")
public class AdminCharacterBannerController {

    private final AdminCharacterBannerService adminCharacterBannerService;

    /**
     * Constructs the AdminCharacterBannerController with the necessary service.
     * @param adminCharacterBannerService The service for administrative character banner operations.
     */
    public AdminCharacterBannerController(AdminCharacterBannerService adminCharacterBannerService) {
        this.adminCharacterBannerService = adminCharacterBannerService;
    }

    /**
     * Creates a new character banner configuration.
     *
     * @param characterBannerCreateDTO The DTO containing the details for the new character banner.
     * @return A ResponseEntity containing the created character banner data or an error message if entities are not found or validation fails.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createCharacterBanner (@Valid @RequestBody CharacterBannerCreateDTO characterBannerCreateDTO) {
        try {
            CharacterBannerResponseDTO characterBannerResponseDTO = adminCharacterBannerService.createCharacterBanner(characterBannerCreateDTO);

            return ResponseEntity.ok(characterBannerResponseDTO);
        } catch (BannerNotFoundException | UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (LimitException | InvalidUnitException | InvalidImageException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * Retrieves a list of all existing character banners.
     *
     * @return A ResponseEntity containing a list of character banner response DTOs.
     */
    @GetMapping
    public ResponseEntity<?> listCharacterBanner() {
        try {
            List<CharacterBannerResponseDTO> characterBannerResponseDTOS = adminCharacterBannerService.listCharacterBanner();

            return ResponseEntity.ok(characterBannerResponseDTOS);
        } catch (LimitException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
        }
    }

    /**
     * Updates an existing character banner configuration by its unique ID.
     *
     * @param id The ID of the character banner to update.
     * @param characterBannerUpdateDTO The DTO containing the updated banner information.
     * @return A ResponseEntity containing the updated character banner data or an error message if not found or validation fails.
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCharacterBanner(@PathVariable Long id, @Valid @RequestBody CharacterBannerUpdateDTO characterBannerUpdateDTO) {
        try {
            CharacterBannerResponseDTO characterBannerResponseDTO = adminCharacterBannerService.updateCharacterBanner(id, characterBannerUpdateDTO);

            return ResponseEntity.ok(characterBannerResponseDTO);
        } catch (BannerNotFoundException | UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (LimitException | InvalidUnitException | InvalidImageException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * Deletes a specific character banner configuration by its unique ID.
     *
     * @param id The ID of the character banner to delete.
     * @return A ResponseEntity containing the deleted character banner data or an error message if not found or deletion is restricted.
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCharacterBanner(@PathVariable Long id) {
        try {
            CharacterBannerResponseDTO characterBannerResponseDTO = adminCharacterBannerService.deleteCharacterBanner(id);

            return ResponseEntity.ok(characterBannerResponseDTO);
        } catch (BannerNotFoundException | UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (LimitException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }
}