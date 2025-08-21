package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new artifact set.
 * This object is used to transfer data from a client's request to the service layer.
 */
@Getter
@Setter
public class ArtifactSetCreateDTO {
    /**
     * The unique name of the artifact set (e.g., "Blizzard Strayer").
     * This field is required.
     */
    @NotBlank(message = "The set name is required")
    @Column(unique = true)
    private String setName;

    /**
     * A URL or file path to the image representing the artifact set.
     * This field is required.
     */
    @NotBlank(message = "The set image is required")
    private String setImage;
}