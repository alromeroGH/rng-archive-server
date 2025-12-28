package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.Pull;
import com.alerom.rng.archive.rng_archive_server.models.enums.BannerTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Pull data.
 * Extends JpaRepository to provide standard CRUD operations and custom queries
 * for managing user summon history and banner-specific statistics.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Repository
public interface PullRepository extends JpaRepository<Pull, Long> {

    /**
     * Retrieves all pulls that are not logically deleted, using eager fetching
     * to load associated users, banners, and units in a single request.
     *
     * @return A list of all active Pull entities with their full relationships.
     */
    @Query("SELECT DISTINCT p FROM Pull p " +
            "JOIN FETCH p.user " +
            "JOIN FETCH p.banner " +
            "JOIN FETCH p.pullsUnits pu " +
            "JOIN FETCH pu.unit " +
            "WHERE p.isDeleted = false")
    List<Pull> findAllPulls();

    /**
     * Finds a specific pull record by its unique ID, provided it is not logically deleted.
     * Fetches all related entities to provide a complete data object.
     *
     * @param id The unique identifier of the pull record.
     * @return An Optional containing the found Pull, or an empty Optional if not found or deleted.
     */
    @Query("SELECT p FROM Pull p " +
            "JOIN FETCH p.user " +
            "JOIN FETCH p.banner " +
            "JOIN FETCH p.pullsUnits pu " +
            "JOIN FETCH pu.unit " +
            "WHERE p.id = :id " +
            "AND p.isDeleted = false")
    Optional<Pull> findPullById(@Param("id") Long id);

    /**
     * Performs a soft delete on a pull record by setting its isDeleted flag to true.
     *
     * @param pull The Pull entity to be logically deleted.
     */
    @Modifying
    @Query("UPDATE Pull p SET p.isDeleted = true WHERE p = :pull")
    void softDeletePull(@Param("pull") Pull pull);

    /**
     * Retrieves a list of pull records for a specific user filtered by the type of banner.
     *
     * @param userId The ID of the user whose history is being retrieved.
     * @param bannerType The type of banner (e.g., STANDARD, CHARACTER_EVENT, WEAPON_EVENT).
     * @return A list of active Pull entities belonging to the user for the specified banner type.
     */
    @Query("SELECT p FROM Pull p " +
            "JOIN FETCH p.banner b " +
            "WHERE p.user.id = :userId " +
            "AND b.bannerType = :bannerType " +
            "AND p.isDeleted = false")
    List<Pull> findUserPullsByBannerType(@Param("userId") Long userId, @Param("bannerType") BannerTypeEnum bannerType);
}