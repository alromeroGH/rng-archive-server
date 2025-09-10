package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.ArtifactPiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtifactPieceRepository extends JpaRepository<ArtifactPiece, Long> {
    @Modifying
    @Query("UPDATE ArtifactPiece ap SET ap.isDeleted = true WHERE ap = :artifactPiece")
    void softDelete(@Param("artifactPiece") ArtifactPiece artifactPiece);

    @Query("SELECT ap FROM ArtifactPiece ap WHERE ap.id = :id AND ap.isDeleted = false")
    Optional<ArtifactPiece> findArtifactPieceById(@Param("id") Long id);
}