package com.alerom.rng.archive.rng_archive_server.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for updating a user's password.
 * This object is used to transfer data from a client's request to the service layer,
 * ensuring the current and new passwords are provided for a secure update.
 */
@Getter
@Setter
public class PasswordUpdateDTO {

    /**
     * The current password of the user.
     * This field is required to confirm the user's identity before updating the password.
     */
    @NotBlank(message = "The current password is required")
    private String currentPassword;

    /**
     * The new password for the user.
     * This field is required and should comply with the application's password policy.
     */
    @NotBlank(message = "The new password is required")
    private String newPassword;
}