package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import com.alerom.rng.archive.rng_archive_server.models.enums.StatTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new stat record.
 * This object is used to transfer data from a client's request to the service layer.
 */
@Getter
@Setter
public class StatCreateDTO {

    /**
     * The name of the stat (e.g., "ATK%", "CRIT Rate").
     * This field is required.
     */
    @NotBlank(message = "The stat name is required")
    private String statName;

    /**
     * The type of the stat, indicating where it can be a main or secondary stat.
     * This field is required.
     */
    @NotNull(message = "The stat type is required")
    private StatTypeEnum statType;
}