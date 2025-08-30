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

@RestController
@RequestMapping("/api/admin/CharacterBanner")
public class AdminCharacterBannerController {

    private final AdminCharacterBannerService adminCharacterBannerService;

    public AdminCharacterBannerController(AdminCharacterBannerService adminCharacterBannerService) {
        this.adminCharacterBannerService = adminCharacterBannerService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCharacterBanner (@Valid @RequestBody CharacterBannerCreateDTO characterBannerCreateDTO) {
        try {
            CharacterBannerResponseDTO characterBannerResponseDTO = adminCharacterBannerService.addCharacterBanner(characterBannerCreateDTO);

            return ResponseEntity.ok(characterBannerResponseDTO);
        } catch (BannerNotFoundException | UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (LimitException | InvalidUnitException | InvalidImageException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> listCharacterBanner() {
        List<CharacterBannerResponseDTO> characterBannerResponseDTOS = adminCharacterBannerService.listCharacterBanner();

        return ResponseEntity.ok(characterBannerResponseDTOS);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?>  updateCharacterBanner(@PathVariable Long id,@Valid @RequestBody CharacterBannerUpdateDTO characterBannerUpdateDTO) {
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