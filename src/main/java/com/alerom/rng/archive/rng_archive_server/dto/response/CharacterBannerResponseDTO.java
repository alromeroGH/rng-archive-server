package com.alerom.rng.archive.rng_archive_server.dto.response;

import com.alerom.rng.archive.rng_archive_server.models.enums.BannerPhaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class CharacterBannerResponseDTO {

    private Long id;
    /**
     * The name of the banner (e.g., "Ballad in Goblets").
     */
    private String bannerName;

    /**
     * The version of the game in which the banner was released (e.g., "1.0", "2.1").
     */
    private String bannerVersion;

    /**
     * The phase of the banner within its version (e.g., ONE, TWO).
     */
    private BannerPhaseEnum bannerPhase;

    /**
     * The start date of the banner.
     */
    private Date bannerStartDate;

    private UnitResponseDTO FiveStartCharacterId;

    private List<UnitResponseDTO> fourStartCharacterIds;

    /**
     * A URL or file path to the image representing the banner.
     */
    private String bannerImage;
}