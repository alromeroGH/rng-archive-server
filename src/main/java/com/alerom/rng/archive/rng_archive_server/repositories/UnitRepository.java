package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.Banner;
import com.alerom.rng.archive.rng_archive_server.models.Unit;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Unit data.
 * Extends CrudRepository to provide standard CRUD operations and specialized queries
 * to filter units by rarity, type, and banner association.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Repository
public interface UnitRepository extends CrudRepository<Unit, Long> {

    /**
     * Retrieves all units that are not logically deleted.
     *
     * @return A list of all active Units.
     */
    @Query("SELECT u FROM Unit u WHERE u.isDeleted = false")
    List<Unit> getAllUnits();

    /**
     * Finds a specific unit by its unique identifier, provided it is not logically deleted.
     *
     * @param id The unit's unique ID.
     * @return An Optional containing the found unit, or an empty Optional if not found or deleted.
     */
    @Query("SELECT u FROM Unit u WHERE u.id = :id AND u.isDeleted = false")
    Optional<Unit> findUnit(@Param("id") Long id);

    /**
     * Performs a soft delete on a unit by setting its isDeleted flag to true.
     *
     * @param unit The Unit entity to be logically deleted.
     */
    @Modifying
    @Query("UPDATE Unit u SET u.isDeleted = true WHERE u = :unit")
    void softDeleteUnit(@Param("unit") Unit unit);

    /**
     * Retrieves the list of limited 5-star characters associated with a specific banner.
     *
     * @param banner The banner to filter by.
     * @return A list of 5-star limited characters for the given banner.
     */
    @Query("SELECT u FROM Unit u " +
            "JOIN FETCH u.bannersUnits bu " +
            "JOIN FETCH bu.banner b " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FIVE_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.CHARACTER " +
            "AND b = :banner")
    List<Unit> getFiveStarLimitedCharacters(@Param("banner") Banner banner);

    /**
     * Retrieves the list of limited 5-star weapons associated with a specific banner.
     *
     * @param banner The banner to filter by.
     * @return A list of 5-star limited weapons for the given banner.
     */
    @Query("SELECT u FROM Unit u " +
            "JOIN FETCH u.bannersUnits bu " +
            "JOIN FETCH bu.banner b " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FIVE_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.WEAPON " +
            "AND b = :banner")
    List<Unit> getFiveStarLimitedWeapons(@Param("banner")Banner banner);

    /**
     * Retrieves the list of standard 5-star characters available in all banners.
     *
     * @return A list of 5-star standard characters.
     */
    @Query("SELECT u FROM Unit u " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FIVE_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.CHARACTER " +
            "AND u.unitBanner = com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum.ALL")
    List<Unit> getFiveStarStandardCharacters();

    /**
     * Retrieves the list of standard 5-star weapons available in all banners.
     *
     * @return A list of 5-star standard weapons.
     */
    @Query("SELECT u FROM Unit u " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FIVE_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.WEAPON " +
            "AND u.unitBanner = com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum.ALL")
    List<Unit> getFiveStarStandardWeapon();

    /**
     * Retrieves the list of featured 4-star characters for a specific banner.
     *
     * @param banner The banner to filter by.
     * @return A list of 4-star limited/featured characters for the given banner.
     */
    @Query("SELECT u FROM Unit u " +
            "JOIN FETCH u.bannersUnits bu " +
            "JOIN FETCH bu.banner b " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FOUR_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.CHARACTER " +
            "AND b = :banner")
    List<Unit> getFourStarLimitedCharacters(@Param("banner") Banner banner);

    /**
     * Retrieves the list of featured 4-star weapons for a specific banner.
     *
     * @param banner The banner to filter by.
     * @return A list of 4-star limited/featured weapons for the given banner.
     */
    @Query("SELECT u FROM Unit u " +
            "JOIN FETCH u.bannersUnits bu " +
            "JOIN FETCH bu.banner b " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FOUR_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.WEAPON " +
            "AND b = :banner")
    List<Unit> getFourStarLimitedWeapon(@Param("banner") Banner banner);

    /**
     * Retrieves the list of standard 4-star units (characters and weapons) available in all banners.
     *
     * @return A list of 4-star standard units.
     */
    @Query("SELECT u FROM Unit u " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FOUR_STARS " +
            "AND u.unitBanner = com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum.ALL")
    List<Unit> getFourStarStandardUnits();

    /**
     * Retrieves the list of standard 3-star weapons available in all banners.
     *
     * @return A list of 3-star standard weapons.
     */
    @Query("SELECT u FROM Unit u " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.THREE_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.WEAPON " +
            "AND u.unitBanner = com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum.ALL")
    List<Unit> getThreeStarStandardWeapons();
}