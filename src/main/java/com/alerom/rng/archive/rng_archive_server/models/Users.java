package com.alerom.rng.archive.rng_archive_server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a user in the system.
 * This entity stores user-related information, including their credentials, unique ID,
 * and relationships to other entities like artifacts and pulls.
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
    /**
     * The unique primary key for the user entity.
     * It is an auto-generated identity value.
     */
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
     * A list of artifacts owned by the user.
     * This establishes a one-to-many relationship with the UsersArtifacts entity.
     */
    @OneToMany(mappedBy = "user")
    private List<UsersArtifacts> usersArtifacts;

    /**
     * A list of all pull records associated with this user.
     * This defines a one-to-many relationship with the Pulls entity.
     */
    @OneToMany(mappedBy = "user")
    private List<Pulls> pulls;

    /**
     * Flag indicating if the user has administrative privileges.
     */
    private Boolean isAdmin;

    /**
     * A boolean flag used for soft deletion, indicating if the user is logically deleted.
     */
    private Boolean isDeleted;
}