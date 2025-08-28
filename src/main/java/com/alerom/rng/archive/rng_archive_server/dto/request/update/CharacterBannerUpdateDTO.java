package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CharacterBannerUpdateDTO {

    @NotNull(message = "The banner id is required")
    private Long bannerUpdateId;

    @NotNull(message = "The five start character id is required")
    private Long fiveStartCharacterUpdateId;

    @NotNull(message = "All the four start characters id are required")
    private List<Long> fourStartCharacterUpdateIds;
}