package com.alerom.rng.archive.rng_archive_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArtifactStatisticsResponseDTO {
    private int totalSetArtifacts;
    private int totalPieceArtifacts;
    private double probabilityPercentage;

//    private Map<String, Double> mainStatDistribution;
//
//    private Map<String, Double> subStatCombinationDistribution;
}