package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new pull record.
 * This object is used to transfer data from a client's request to the service layer.
 */
@Getter
@Setter
public class PullCreateDTO {

    /**
     * The number of pulls performed in this event.
     * This field is required.
     */
    @NotNull(message = "The pull amount is required")
    private int pullsAmount;

    /**
     * A boolean flag indicating whether the user won the 50/50 chance for a limited 5-star unit.
     * This field is required.
     */
    @NotNull(message = "The won is required")
    private Boolean won;

    /**
     * A boolean flag indicating whether the user activated the "Capturing Radiance" mechanic.
     * This is used for weapon banners with the Epitomized Path.
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