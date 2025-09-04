package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.dto.response.UnitResponseDTO;
import com.alerom.rng.archive.rng_archive_server.models.Unit;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends CrudRepository<Unit, Long> {
    @Query("SELECT u FROM Unit u WHERE u.isDeleted = false")
    List<Unit> getAllUnits();

    @Query("SELECT u FROM Unit u WHERE u.id = :id AND u.isDeleted = false")
    Optional<Unit> findUnit(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Unit u SET u.isDeleted = true WHERE u = :unit")
    void softDeleteUnit(@Param("unit") Unit unit);
}