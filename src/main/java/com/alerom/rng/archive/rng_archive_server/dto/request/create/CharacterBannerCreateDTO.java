package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CharacterBannerCreateDTO {

    @NotNull(message = "The banner id is required")
    private Long bannerId;

    @NotNull(message = "The five start character is required")
    private Long fiveStartCharacterId;

    @NotNull(message = "All the four start characters are required")
    private List<Long> fourStartCharacterIds;
}