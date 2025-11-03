package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummonWeaponEventCreateDTO {
    @NotNull(message = "The bannerId is required")
    private Long bannerId;

    @NotNull(message = "The summonAmount is required")
    private int summonAmount;

    @NotNull(message = "The pityCount is required")
    private int pityCount;

    @NotNull(message = "The primoCount is required")
    private int primoCount;

    @NotNull(message = "The divinePathCount is required")
    private int divinePathCount;

    @NotNull(message = "The weaponSelected is required")
    private Long weaponSelected;

    @NotNull(message = "The fourStarPityCount is required")
    private int fourStarPityCount;
}