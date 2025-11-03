package com.alerom.rng.archive.rng_archive_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SummonWeaponEventResponseDTO {
    private List<UnitResponseDTO> units;
    private int pityCount;
    private int primoCount;
    private int divinePathCount;
    private int winFourStarCount;
}