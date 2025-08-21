package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating an existing user.
 * This object is used to transfer data from a client's request to the service layer,
 * allowing for updates to a user's properties like username, email, or UID.
 */
@Getter
@Setter
public class UserUpdateDTO {

    /**
     * The new username for the user.
     * This field is required.
     */
    @NotBlank(message = "The username is required")
    private String userName;

    /**
     * The new email address for the user. Must be a valid format.
     * This field is required.
     */
    @Email
    @NotBlank(message = "The email is required")
    private String email;

    /**
     * The new unique identifier (UID) for the user.
     * This field is required.
     */
    @NotBlank(message = "The UID is required")
    private String uid;
}