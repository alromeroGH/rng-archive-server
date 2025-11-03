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

@Repository
public interface UnitRepository extends CrudRepository<Unit, Long> {
    @Query("SELECT u FROM Unit u WHERE u.isDeleted = false")
    List<Unit> getAllUnits();

    @Query("SELECT u FROM Unit u WHERE u.id = :id AND u.isDeleted = false")
    Optional<Unit> findUnit(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Unit u SET u.isDeleted = true WHERE u = :unit")
    void softDeleteUnit(@Param("unit") Unit unit);

    @Query("SELECT u FROM Unit u " +
            "JOIN FETCH u.bannersUnits bu " +
            "JOIN FETCH bu.banner b " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FIVE_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.CHARACTER " +
            "AND b = :banner")
    List<Unit> getFiveStarLimitedCharacters(@Param("banner") Banner banner);

    @Query("SELECT u FROM Unit u " +
            "JOIN FETCH u.bannersUnits bu " +
            "JOIN FETCH bu.banner b " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FIVE_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.WEAPON " +
            "AND b = :banner")
    List<Unit> getFiveStarLimitedWeapons(@Param("banner")Banner banner);

    @Query("SELECT u FROM Unit u " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FIVE_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.CHARACTER " +
            "AND u.unitBanner = com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum.ALL")
    List<Unit> getFiveStarStandardCharacters();

    @Query("SELECT u FROM Unit u " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FIVE_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.WEAPON " +
            "AND u.unitBanner = com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum.ALL")
    List<Unit> getFiveStarStandardWeapon();

    @Query("SELECT u FROM Unit u " +
            "JOIN FETCH u.bannersUnits bu " +
            "JOIN FETCH bu.banner b " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FOUR_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.CHARACTER " +
            "AND b = :banner")
    List<Unit> getFourStarLimitedCharacters(@Param("banner") Banner banner);

    @Query("SELECT u FROM Unit u " +
            "JOIN FETCH u.bannersUnits bu " +
            "JOIN FETCH bu.banner b " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FOUR_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.WEAPON " +
            "AND b = :banner")
    List<Unit> getFourStarLimitedWeapon(@Param("banner") Banner banner);

    @Query("SELECT u FROM Unit u " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.FOUR_STARS " +
            "AND u.unitBanner = com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum.ALL")
    List<Unit> getFourStarStandardUnits();

    @Query("SELECT u FROM Unit u " +
            "WHERE u.isDeleted = false " +
            "AND u.numberOfStars = com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum.THREE_STARS " +
            "AND u.unitType = com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum.WEAPON " +
            "AND u.unitBanner = com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum.ALL")
    List<Unit> getThreeStarStandardWeapons();
}