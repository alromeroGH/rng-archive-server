package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.ArtifactSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtifactSetRepository extends JpaRepository<ArtifactSet, Long> {
    @Query("SELECT DISTINCT as FROM ArtifactSet as JOIN as.artifactPieces ap WHERE as.isDeleted = false AND ap.isDeleted = false")
    List<ArtifactSet> findAllArtifacts();

    @Query("SELECT as FROM ArtifactSet as JOIN as.artifactPieces ap WHERE as.id = :id AND as.isDeleted = false AND ap.isDeleted = false")
    Optional<ArtifactSet> findByArtifactId(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ArtifactSet as SET as.isDeleted = true WHERE as = :artifactSet")
    void softDelete(@Param("artifactSet") ArtifactSet artifactSet);
}