package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.ArtifactSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ArtifactSet data.
 * Extends JpaRepository to provide standard CRUD operations and custom queries
 * for managing sets of artifacts and their associated pieces.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Repository
public interface ArtifactSetRepository extends JpaRepository<ArtifactSet, Long> {

    /**
     * Retrieves all artifact sets that are not logically deleted and have active associated pieces.
     * Uses DISTINCT to avoid duplicate results from the join operation.
     *
     * @return A list of active ArtifactSet entities.
     */
    @Query("SELECT DISTINCT as FROM ArtifactSet as JOIN as.artifactPieces ap WHERE as.isDeleted = false AND ap.isDeleted = false")
    List<ArtifactSet> findAllArtifacts();

    /**
     * Finds a specific artifact set by its unique ID, ensuring both the set and its pieces are not deleted.
     *
     * @param id The unique identifier of the artifact set.
     * @return An Optional containing the found ArtifactSet, or an empty Optional if not found or deleted.
     */
    @Query("SELECT as FROM ArtifactSet as JOIN as.artifactPieces ap WHERE as.id = :id AND as.isDeleted = false AND ap.isDeleted = false")
    Optional<ArtifactSet> findByArtifactId(@Param("id") Long id);

    /**
     * Performs a soft delete on an artifact set by setting its isDeleted flag to true.
     *
     * @param artifactSet The ArtifactSet entity to be logically deleted.
     */
    @Modifying
    @Query("UPDATE ArtifactSet as SET as.isDeleted = true WHERE as = :artifactSet")
    void softDelete(@Param("artifactSet") ArtifactSet artifactSet);
}