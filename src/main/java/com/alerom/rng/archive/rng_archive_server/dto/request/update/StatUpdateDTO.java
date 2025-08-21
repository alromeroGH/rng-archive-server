package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import com.alerom.rng.archive.rng_archive_server.models.enums.StatTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing stat record.
 * This object is used to transfer data from a client's request to the service layer,
 * allowing for partial updates of a stat's properties.
 */
@Getter
@Setter
public class StatUpdateDTO {

    /**
     * The new name of the stat (e.g., "CRIT Rate", "ATK%").
     * This field is required.
     */
    @NotBlank(message = "The stat name is required")
    private String statName;

    /**
     * The new type of the stat, indicating where it can be a main or secondary stat.
     * This field is required.
     */
    @NotNull(message = "The stat type is required")
    private StatTypeEnum statType;
}