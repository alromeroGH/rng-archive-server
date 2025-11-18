package com.alerom.rng.archive.rng_archive_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class PullStatisticResponseDTO {
    private int totalPull;
    private int totalFiveUnit;
    private int totalLimitedUnit;

    private Map<String, Integer> fiftyFiftyGraphic;
    private Map<String, Integer> capturingRadianceGraphic;
}