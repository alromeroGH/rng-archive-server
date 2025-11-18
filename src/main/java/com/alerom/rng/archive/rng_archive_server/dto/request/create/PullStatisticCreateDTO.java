package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import com.alerom.rng.archive.rng_archive_server.models.enums.BannerTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PullStatisticCreateDTO {

    @NotNull(message = "The userId is required")
    private Long userId;

    @NotNull(message = "The bannerTypeEnum is required")
    private BannerTypeEnum bannerType;
}