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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summon-simulator")
public class SummonSimulatorController {
    private final SummonSimulatorService summonSimulatorService;

    public SummonSimulatorController(SummonSimulatorService summonSimulatorService) {
        this.summonSimulatorService = summonSimulatorService;
    }

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