package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.SecondaryStat;
import com.alerom.rng.archive.rng_archive_server.models.UserArtifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for SecondaryStat data.
 * Extends JpaRepository to provide standard CRUD operations for the sub-stats
 * associated with a user's artifact.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Repository
public interface SecondaryStatRepository extends JpaRepository<SecondaryStat, Long> {

    /**
     * Performs a soft delete on a secondary stat by setting its isDeleted flag to true.
     *
     * @param secondaryStat The SecondaryStat entity to be logically deleted.
     */
    @Modifying
    @Query("UPDATE SecondaryStat ss SET ss.isDeleted = true WHERE ss = :secondaryStat")
    void softDeleteSecondaryStat(@Param("secondaryStat") SecondaryStat secondaryStat);
}