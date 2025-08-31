package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import com.alerom.rng.archive.rng_archive_server.models.enums.BannerPhaseEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CharacterBannerCreateDTO {

    @NotBlank(message = "The banner name is required")
    private String bannerName;

    @NotBlank(message = "The banner version is required")
    private String bannerVersion;

    @NotNull(message = "The banner phase is required")
    private BannerPhaseEnum bannerPhase;

    @NotNull(message = "The banner start date is required")
    private Date bannerStartDate;

    @NotNull(message = "The five start character id is required")
    private Long fiveStarCharacterId;

    @NotNull(message = "All the four start characters ids are required")
    private List<Long> fourStarCharacterIds;

    @NotBlank(message = "The banner image is required")
    private String bannerImage;
}