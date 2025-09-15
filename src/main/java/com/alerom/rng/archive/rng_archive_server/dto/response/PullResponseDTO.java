package com.alerom.rng.archive.rng_archive_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for retrieving pull record data.
 * This object is used to transfer a complete representation of a pull event from the service layer to the client.
 */
@Getter
@AllArgsConstructor
public class PullResponseDTO {

    /**
     * The unique identifier of the pull record.
     */
    private Long id;

    /**
     * The number of pulls performed in this event.
     */
    private int pullsAmount;

    /**
     * A boolean flag indicating whether the user won the 50/50 chance for a limited 5-star unit.
     */
    private Boolean won;

    /**
     * A boolean flag indicating whether the user activated the "Capturing Radiance" mechanic.
     */
    private Boolean activatedCapturingRadiance;

    /**
     * The unique identifier of the user who performed the pull.
     */
    private UserResponseDTO userResponseDTO;

    /**
     * The unique identifier of the banner on which the pull was performed.
     */
    private BannerResponseDTO bannerResponseDTO;

    private UnitResponseDTO unitResponseDTO;
}