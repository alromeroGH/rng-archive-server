package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import com.alerom.rng.archive.rng_archive_server.models.enums.NewsTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new news item.
 * This object is used to transfer data from a client's request to the service layer.
 */
@Getter
@Setter
public class NewsCreateDTO {

    /**
     * The type of the news item (e.g., EVENT, BANNER, CODE).
     * This field is required.
     */
    @NotNull(message = "The new type is required")
    private NewsTypeEnum newsType;

    /**
     * A brief description or summary of the news item.
     * This field is required.
     */
    @NotBlank(message = "The description is required")
    private String description;

    /**
     * A URL or external link for the full details of the news item.
     * This field is optional.
     */
    private String link;
}