package com.alerom.rng.archive.rng_archive_server.controllers;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.SummonCharacterEventCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.create.SummonWeaponEventCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.SummonCharacterEventResponseDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.SummonWeaponEventResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.BannerNotFoundException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UnitNotFoundException;
import com.alerom.rng.archive.rng_archive_server.services.SummonSimulatorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing summon simulation operations.
 * It provides endpoints to simulate character and weapon summons based on specific events.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/summon-simulator")
public class SummonSimulatorController {
    private final SummonSimulatorService summonSimulatorService;

    /**
     * Constructs the SummonSimulatorController with the necessary service.
     * @param summonSimulatorService The service for handling summon simulation logic.
     */
    public SummonSimulatorController(SummonSimulatorService summonSimulatorService) {
        this.summonSimulatorService = summonSimulatorService;
    }

    /**
     * Simulates a summon on a character event banner.
     *
     * @param summonCharacterEventCreateDTO The DTO containing the character summon parameters.
     * @return A ResponseEntity containing the result of the character summon or an error message if the banner or units are not found.
     */
    @PostMapping("/character")
    public ResponseEntity<?> generalCharacterEventSummon(@Valid @RequestBody SummonCharacterEventCreateDTO summonCharacterEventCreateDTO) {
        try {
            SummonCharacterEventResponseDTO summonCharacterEventResponseDTO = summonSimulatorService.generalCharacterEventSummon(summonCharacterEventCreateDTO);

            return ResponseEntity.ok(summonCharacterEventResponseDTO);
        } catch (BannerNotFoundException | UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    /**
     * Simulates a summon on a weapon event banner.
     *
     * @param summonWeaponEventCreateDTO The DTO containing the weapon summon parameters.
     * @return A ResponseEntity containing the result of the weapon summon or an error message if the banner or units are not found.
     */
    @PostMapping("/weapon")
    public ResponseEntity<?> generalWeaponEventSummon(@Valid @RequestBody SummonWeaponEventCreateDTO summonWeaponEventCreateDTO) {
        try {
            SummonWeaponEventResponseDTO summonWeaponEventResponseDTO = summonSimulatorService.generalWeaponEventSummon(summonWeaponEventCreateDTO);

            return ResponseEntity.ok(summonWeaponEventResponseDTO);
        } catch (BannerNotFoundException | UnitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}