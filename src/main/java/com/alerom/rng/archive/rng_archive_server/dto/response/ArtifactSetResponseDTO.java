package com.alerom.rng.archive.rng_archive_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for retrieving artifact set data.
 * This object is used to transfer a complete representation of an artifact set from the service layer to the client.
 */
@Getter
@AllArgsConstructor
public class ArtifactSetResponseDTO {

    /**
     * The unique identifier of the artifact set.
     */
    private Long id;

    /**
     * The name of the artifact set (e.g., "Blizzard Strayer").
     */
    private String setName;

    /**
     * A URL or file path to the image representing the artifact set.
     */
    private String setImage;
}