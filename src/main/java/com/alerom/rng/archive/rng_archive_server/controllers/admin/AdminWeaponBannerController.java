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

@RestController
@RequestMapping("/api/admin/weaponBanner")
public class AdminWeaponBannerController {
    private final AdminWeaponBannerService adminWeaponBannerService;


    public AdminWeaponBannerController(AdminWeaponBannerService adminWeaponBannerService) {
        this.adminWeaponBannerService = adminWeaponBannerService;
    }

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