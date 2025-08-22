package com.alerom.rng.archive.rng_archive_server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents an artifact set, which is a collection of artifact pieces.
 * This entity stores information about the set, including its name and image,
 * and its relationship to the individual artifact pieces that belong to it.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artifact_sets")
public class ArtifactSet {
    /**
     * The unique primary key for the artifact set entity.
     * It is an auto-generated identity value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique name of the artifact set (e.g., "Blizzard Strayer").
     */
    private String setName;

    /**
     * A URL or file path to the image representing the artifact set.
     */
    private String setImage;

    /**
     * A list of all artifact pieces that are part of this set.
     * This defines a one-to-many relationship with the ArtifactPieces entity.
     */
    @OneToMany(mappedBy = "artifactSet")
    private List<ArtifactPiece> artifactPieces;

    /**
     * A boolean flag used for soft deletion, indicating if the artifact set is logically deleted.
     */
    private Boolean isDeleted;
}