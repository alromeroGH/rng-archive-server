package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.UserArtifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserArtifact data.
 * Extends JpaRepository to provide standard CRUD operations and custom queries for artifact management.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Repository
public interface UserArtifactRepository extends JpaRepository<UserArtifact, Long> {

    /**
     * Retrieves all user artifacts that are not logically deleted, fetching related entities to avoid N+1 issues.
     *
     * @return A list of all active UserArtifacts with their associated user, stats, pieces, and sets.
     */
    @Query("SELECT ua FROM UserArtifact ua " +
            "JOIN FETCH ua.user " +
            "JOIN FETCH ua.stat " +
            "JOIN FETCH ua.artifactPiece ap " +
            "JOIN FETCH ap.artifactSet " +
            "JOIN FETCH ua.secondaryStats ss " +
            "WHERE ua.isDeleted = false " +
            "AND ss.isDeleted = false")
    List<UserArtifact> listAllUserArtifacts();

    /**
     * Finds a specific user artifact by its ID, provided it is not logically deleted.
     *
     * @param id The unique identifier of the user artifact.
     * @return An Optional containing the found UserArtifact, or an empty Optional if not found or deleted.
     */
    @Query("SELECT ua FROM UserArtifact ua WHERE ua.id = :id AND ua.isDeleted = false")
    Optional<UserArtifact> findUserArtifactById(@Param("id") Long id);

    /**
     * Performs a soft delete on a user artifact by setting its isDeleted flag to true.
     *
     * @param userArtifact The UserArtifact entity to be logically deleted.
     */
    @Modifying
    @Query("UPDATE UserArtifact ua SET ua.isDeleted = true WHERE ua = :userArtifact")
    void softDeleteUserArtifact(@Param("userArtifact") UserArtifact userArtifact);

    /**
     * Finds all active artifacts belonging to a specific user and artifact set.
     *
     * @param userId The ID of the owner user.
     * @param setId The ID of the artifact set.
     * @return A list of UserArtifacts that match the user and set criteria.
     */
    @Query("SELECT ua FROM UserArtifact ua " +
            "JOIN FETCH ua.artifactPiece ap " +
            "JOIN FETCH ap.artifactSet ar " +
            "WHERE ua.user.id = :userId " +
            "AND ar.id = :setId " +
            "AND ar.isDeleted = false")
    List<UserArtifact> findArtifactsByUserAndSet(@Param("userId") Long userId, @Param("setId") Long setId);

    /**
     * Finds all active artifacts belonging to a specific user and a specific artifact piece.
     *
     * @param userId The ID of the owner user.
     * @param artifactPieceId The ID of the specific artifact piece.
     * @return A list of UserArtifacts that match the user and piece criteria.
     */
    @Query("SELECT ua FROM UserArtifact ua " +
            "JOIN FETCH ua.artifactPiece ap " +
            "WHERE ua.user.id = :userId " +
            "AND ap.id = :artifactPieceId " +
            "AND ap.isDeleted = false")
    List<UserArtifact> findArtifactsByUserAndPiece(@Param("userId") Long userId, @Param("artifactPieceId") Long artifactPieceId);
}