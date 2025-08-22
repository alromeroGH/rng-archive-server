package com.alerom.rng.archive.rng_archive_server.models;

import com.alerom.rng.archive.rng_archive_server.models.enums.PieceTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a specific piece of an artifact set, such as a flower, feather, or goblet.
 * This entity stores information about the type and name of the artifact piece,
 * and its relationship to an artifact set.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artifact_pieces")
public class ArtifactPiece {
    /**
     * The unique primary key for the artifact piece entity.
     * It is an auto-generated identity value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The type of the artifact piece (e.g., Flower, Plume, Sands, Goblet, Circlet).
     * The value is stored as a string representation of the PieceTypeEnum.
     */
    @Enumerated(EnumType.STRING)
    private PieceTypeEnum pieceType;

    /**
     * The name of the specific artifact piece (e.g., "Flower of Life").
     */
    private String pieceName;

    /**
     * The artifact set to which this piece belongs.
     * This establishes a many-to-one relationship with the ArtifactSets entity.
     */
    @ManyToOne
    @JoinColumn(name = "set_id")
    private ArtifactSet artifactSet;

    /**
     * A list of user-owned instances of this artifact piece.
     * This defines a one-to-many relationship with the UsersArtifacts entity.
     */
    @OneToMany(mappedBy = "artifactPiece")
    private List<UserArtifact> usersArtifacts;

    /**
     * A boolean flag used for soft deletion, indicating if the artifact piece is logically deleted.
     */
    private Boolean isDeleted;
}