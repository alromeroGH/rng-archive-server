package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.dto.response.UserArtifactResponseDTO;
import com.alerom.rng.archive.rng_archive_server.models.UserArtifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserArtifactRepository extends JpaRepository<UserArtifact, Long> {
    @Query("SELECT ua FROM UserArtifact ua " +
            "JOIN FETCH ua.user " +
            "JOIN FETCH ua.stat " +
            "JOIN FETCH ua.artifactPiece ap " +
            "JOIN FETCH ap.artifactSet " +
            "JOIN FETCH ua.secondaryStats ss " +
            "WHERE ua.isDeleted = false " +
            "AND ss.isDeleted = false")
    List<UserArtifact> listAllUserArtifacts();

    @Query("SELECT ua FROM UserArtifact ua WHERE ua.id = :id AND ua.isDeleted = false")
    Optional<UserArtifact> findUserArtifactById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE UserArtifact ua SET ua.isDeleted = true WHERE ua = :userArtifact")
    void softDeleteUserArtifact(@Param("userArtifact") UserArtifact userArtifact);
}