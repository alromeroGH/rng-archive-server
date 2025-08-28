package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing artifact set.
 * This object is used to transfer data from a client's request to the service layer,
 * allowing for partial updates of an artifact set's properties.
 */
@Getter
@Setter
public class ArtifactSetUpdateDTO {

    /**
     * The new name of the artifact set.
     * This field is required.
     */
    @NotBlank(message = "The set name is required")
    @Column(unique = true)
    private String setName;

    /**
     * The new URL or file path to the image representing the artifact set.
     * This field is required.
     */
    @NotBlank(message = "The set image is required")
    @Column(unique = true)
    private String setImage;
}