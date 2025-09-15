package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.Pull;
import com.alerom.rng.archive.rng_archive_server.models.PullUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PullUnitRepository extends JpaRepository<PullUnit, Long> {
    @Modifying
    @Query("UPDATE PullUnit pu SET pu.isDeleted = true WHERE pu = :pullUnit")
    void softDeletePullUnit(@Param("pullUnit") PullUnit pullUnit);
}