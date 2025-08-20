package com.alerom.rng.archive.rng_archive_server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a secondary stat on a user-owned artifact.
 * This entity links a user's specific artifact piece to a particular stat,
 * defining the secondary stats that an artifact piece possesses.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "secondary_stats")
public class SecondaryStats {
    /**
     * The unique primary key for the secondary stat entity.
     * It is an auto-generated identity value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The specific stat (e.g., ATK%, CRIT DMG) that is a secondary attribute.
     * This establishes a many-to-one relationship with the Stats entity.
     */
    @ManyToOne
    @JoinColumn(name = "stat_id")
    private Stats stat;

    /**
     * The user's specific artifact piece to which this secondary stat belongs.
     * This establishes a many-to-one relationship with the UsersArtifacts entity.
     */
    @ManyToOne
    @JoinColumn(name = "user_artifact_id")
    private UsersArtifacts usersArtifacts;

    /**
     * A boolean flag used for soft deletion, indicating if the secondary stat record is logically deleted.
     */
    private Boolean isDeleted;
}