package com.alerom.rng.archive.rng_archive_server.dto.response;

import com.alerom.rng.archive.rng_archive_server.models.enums.BannerPhaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class WeaponBannerResponseDTO {

    private Long id;

    private String bannerName;

    private String bannerVersion;

    private BannerPhaseEnum bannerPhase;

    private Date bannerStartDate;

    private List<UnitResponseDTO> fiveStarWeapons;

    private List<UnitResponseDTO> fourStarWeapons;

    private String bannerImage;
}