package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Stat data.
 * Extends JpaRepository to provide standard CRUD operations for statistics
 * related to artifacts and units.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

    /**
     * Finds a specific statistic by its unique identifier, provided it is not logically deleted.
     *
     * @param id The unique ID of the statistic.
     * @return An Optional containing the found statistic, or an empty Optional if not found or deleted.
     */
    @Query("SELECT s FROM Stat s WHERE s.id = :id AND s.isDeleted = false")
    Optional<Stat> findStatById(@Param("id") Long id);

    /**
     * Retrieves all statistics that are not logically deleted.
     *
     * @return A list of all active Stat entities.
     */
    @Query("SELECT s FROM Stat s WHERE s.isDeleted = false")
    List<Stat> getAllStats();
}