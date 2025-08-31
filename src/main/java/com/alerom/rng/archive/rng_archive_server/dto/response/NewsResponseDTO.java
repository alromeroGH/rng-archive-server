package com.alerom.rng.archive.rng_archive_server.dto.response;

import com.alerom.rng.archive.rng_archive_server.models.enums.NewsTypeEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * DTO for retrieving news item data.
 * This object is used to transfer a complete representation of a news item from the service layer to the client.
 */
@Getter
@AllArgsConstructor
public class NewsResponseDTO {

    /**
     * The unique identifier of the news item.
     */
    private Long id;

    /**
     * The type of the news item (e.g., EVENT, BANNER, CODE).
     */
    private NewsTypeEnum newsType;

    /**
     * The title of the news item.
     */
    private String title;

    /**
     * A brief description or summary of the news item.
     */
    private String description;

    /**
     * A URL or external link for the full details of the news item.
     */
    private String link;

    /**
     * The date when the news item was published.
     */
    private Date dateOfPublication;
}