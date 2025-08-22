package com.alerom.rng.archive.rng_archive_server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a specific artifact instance owned by a user.
 * This entity stores a link between a user, an artifact piece, its main stat,
 * and a collection of its secondary stats.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_artifacts")
public class UserArtifact {
    /**
     * The unique primary key for the user's artifact entity.
     * It is an auto-generated identity value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who owns this artifact.
     * This establishes a many-to-one relationship with the Users entity.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The main stat of this artifact.
     * This establishes a many-to-one relationship with the Stats entity.
     */
    @ManyToOne
    @JoinColumn(name = "main_stat_id")
    private Stat stat;

    /**
     * The specific piece of the artifact (e.g., Flower of Life from a certain set).
     * This establishes a many-to-one relationship with the ArtifactPieces entity.
     */
    @ManyToOne
    @JoinColumn(name = "artifact_piece_id")
    private ArtifactPiece artifactPiece;

    /**
     * A list of all secondary stats associated with this artifact.
     * This defines a one-to-many relationship with the SecondaryStats entity.
     */
    @OneToMany(mappedBy = "userArtifact")
    private List<SecondaryStat> secondaryStats;

    /**
     * A boolean flag used for soft deletion, indicating if the artifact is logically deleted.
     */
    private Boolean isDeleted;
}