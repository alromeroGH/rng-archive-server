package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummonCharacterEventCreateDTO {
    @NotNull(message = "The bannerId is required")
    private Long bannerId;

    @NotNull(message = "The summonAmount is required")
    private int summonAmount;

    @NotNull(message = "The pityCount is required")
    private int pityCount;

    @NotNull(message = "The primoCount is required")
    private int primoCount;

    @NotNull(message = "The winFiftyFiftyCount is required")
    private int winFiftyFiftyCount;

    @NotNull(message = "The winCapturingRadianceCount is required")
    private int winCapturingRadianceCount;

    @NotNull(message = "The isLostFiftyFifty is required")
    private boolean isLostFiftyFifty;

    @NotNull(message = "The fourStarPityCount is required")
    private int fourStarPityCount;
}