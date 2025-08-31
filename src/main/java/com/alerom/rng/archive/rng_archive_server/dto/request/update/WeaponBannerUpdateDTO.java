package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import com.alerom.rng.archive.rng_archive_server.models.enums.BannerPhaseEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class WeaponBannerUpdateDTO {

    @NotBlank(message = "The banner name is required")
    private String bannerName;

    @NotBlank(message = "The banner version is required")
    private String bannerVersion;

    @NotNull(message = "The banner phase is required")
    private BannerPhaseEnum bannerPhase;

    @NotNull(message = "The banner start date is required")
    private Date bannerStartDate;

    @NotNull(message = "All the five start characters ids are required")
    private List<Long> fiveStarWeaponIds;

    @NotNull(message = "All the four start characters ids are required")
    private List<Long> fourStarWeaponIds;

    @NotBlank(message = "The banner image is required")
    private String bannerImage;
}