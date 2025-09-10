package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.SecondaryStat;
import com.alerom.rng.archive.rng_archive_server.models.UserArtifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondaryStatRepository extends JpaRepository<SecondaryStat, Long> {
    @Modifying
    @Query("UPDATE SecondaryStat ss SET ss.isDeleted = true WHERE ss = :secondaryStat")
    void softDeleteSecondaryStat(@Param("secondaryStat") SecondaryStat secondaryStat);
}