package com.alerom.rng.archive.rng_archive_server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the User entity in the database.
 * This class stores user-related information, including credentials and roles.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username of the user.
     */
    private String userName;

    /**
     * The unique email address of the user. Used for login and identification.
     */
    private String email;

    /**
     * The hashed password of the user. Stored securely.
     */
    private String password;

    /**
     * The unique identifier (UID) of the user inside Genshin Impact.
     */
    private String uid;

    /**
     * Flag indicating if the user has administrative privileges.
     */
    private Boolean isAdmin;

    /**
     * Flag indicating if the user account is marked as deleted.
     */
    private Boolean isDeleted;
}