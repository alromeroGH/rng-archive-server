package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.PullUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for PullUnit data.
 * Extends JpaRepository to provide standard CRUD operations for the mapping
 * between pull events and the specific units obtained.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Repository
public interface PullUnitRepository extends JpaRepository<PullUnit, Long> {

    /**
     * Performs a soft delete on a pull-unit relationship by setting its isDeleted flag to true.
     *
     * @param pullUnit The PullUnit entity to be logically deleted.
     */
    @Modifying
    @Query("UPDATE PullUnit pu SET pu.isDeleted = true WHERE pu = :pullUnit")
    void softDeletePullUnit(@Param("pullUnit") PullUnit pullUnit);
}