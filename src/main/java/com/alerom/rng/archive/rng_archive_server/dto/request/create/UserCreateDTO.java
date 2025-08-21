package com.alerom.rng.archive.rng_archive_server.dto.request.create;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new user.
 * This object is used to transfer data from a client's registration request to the service layer.
 */
@Getter
@Setter
public class UserCreateDTO {

    /**
     * The username for the new user.
     * This field is required.
     */
    @NotBlank(message = "The username is required")
    private String userName;

    /**
     * The user's email address. Must be a valid format and unique across all users.
     * This field is required.
     */
    @Email
    @NotBlank(message = "The email is required")
    private String email;

    /**
     * The user's password.
     * This field is required.
     */
    @NotBlank(message = "The password is required")
    private String password;

    /**
     * The unique identifier (UID) for the user, the Genshin Impact UID.
     * This field is required.
     */
    @NotBlank(message = "The UID is required")
    private String uid;

    /**
     * A boolean flag indicating if the user is an administrator.
     * This field is optional.
     */
    private Boolean isAdmin;
}