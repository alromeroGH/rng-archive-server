package com.alerom.rng.archive.rng_archive_server.dto.response;

import com.alerom.rng.archive.rng_archive_server.models.enums.StatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for retrieving stat data.
 * This object is used to transfer a complete representation of a stat
 * from the service layer to the client.
 */
@Getter
@AllArgsConstructor
public class StatResponseDTO {

    /**
     * The unique identifier of the stat.
     */
    private Long id;

    /**
     * The name of the stat (e.g., "CRIT Rate", "ATK%").
     */
    private String statName;

    /**
     * The type of the stat, indicating where it can be a main or secondary stat.
     */
    private StatTypeEnum statType;
}