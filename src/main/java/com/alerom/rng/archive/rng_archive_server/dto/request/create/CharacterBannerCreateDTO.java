package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import com.alerom.rng.archive.rng_archive_server.models.enums.BannerPhaseEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CharacterBannerCreateDTO {

    private String bannerName;

    private String bannerVersion;

    private BannerPhaseEnum bannerPhase;

    private Date bannerStartDate;

    @NotNull(message = "The five start character id is required")
    private Long fiveStarCharacterId;

    @NotNull(message = "All the four start characters ids are required")
    private List<Long> fourStarCharacterIds;

    private String bannerImage;
}