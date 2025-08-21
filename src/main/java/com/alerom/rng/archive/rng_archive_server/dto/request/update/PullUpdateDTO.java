package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing pull record.
 * This object is used to transfer data from a client's request to the service layer,
 * allowing for partial updates of a pull's properties.
 */
@Getter
@Setter
public class PullUpdateDTO {

    /**
     * The new number of pulls performed in this event.
     * This field is required.
     */
    @NotNull(message = "The pull amount is required")
    private int pullsAmount;

    /**
     * The new boolean flag indicating whether the user won the 50/50 chance.
     * This field is required.
     */
    @NotNull(message = "The won is required")
    private Boolean won;

    /**
     * The new boolean flag indicating whether the user activated the "Capturing Radiance" mechanic.
     * This field is required.
     */
    @NotNull(message = "The activated capturing radiance is required")
    private Boolean activatedCapturingRadiance;

    /**
     * The unique identifier of the banner on which the pull was performed.
     * This field is required.
     */
    @NotNull(message = "The banner id is required")
    private Long bannerId;
}