package com.alerom.rng.archive.rng_archive_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for retrieving user data.
 * This object is used to transfer a complete representation of a user from the service layer to the client.
 */
@Getter
@AllArgsConstructor
public class UserResponseDTO {

    /**
     * The unique identifier of the user.
     */
    private Long id;

    /**
     * The unique username of the user.
     */
    private String userName;

    /**
     * The unique email address of the user.
     */
    private String email;

    /**
     * The unique identifier (UID) for the user, typically from an external authentication provider.
     */
    private String uid;

    /**
     * A boolean flag indicating if the user has administrator privileges.
     */
    private Boolean isAdmin;
}