package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.ArtifactPiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for ArtifactPiece data.
 * Extends JpaRepository to provide standard CRUD operations for individual
 * artifact pieces belonging to a set.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Repository
public interface ArtifactPieceRepository extends JpaRepository<ArtifactPiece, Long> {

    /**
     * Performs a soft delete on a specific artifact piece by setting its isDeleted flag to true.
     *
     * @param artifactPiece The ArtifactPiece entity to be logically deleted.
     */
    @Modifying
    @Query("UPDATE ArtifactPiece ap SET ap.isDeleted = true WHERE ap = :artifactPiece")
    void softDelete(@Param("artifactPiece") ArtifactPiece artifactPiece);

    /**
     * Finds a specific artifact piece by its unique identifier, provided it is not logically deleted.
     *
     * @param id The unique identifier of the artifact piece.
     * @return An Optional containing the found ArtifactPiece, or an empty Optional if not found or deleted.
     */
    @Query("SELECT ap FROM ArtifactPiece ap WHERE ap.id = :id AND ap.isDeleted = false")
    Optional<ArtifactPiece> findArtifactPieceById(@Param("id") Long id);
}