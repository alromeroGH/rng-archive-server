package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Banner data.
 * Extends JpaRepository to provide standard CRUD operations and specialized queries
 * for character and weapon banners.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    /**
     * Retrieves all active character banners that have associated units,
     * ordered by their start date in descending order.
     *
     * @return A list of active character Banner entities.
     */
    @Query("SELECT DISTINCT b FROM Banner b JOIN b.bannersUnits bu WHERE b.bannerType = 'limited_character' AND b.isDeleted = false AND bu.isDeleted = false ORDER BY b.bannerStartDate DESC")
    List<Banner> findCharacterBanners();

    /**
     * Retrieves all active weapon banners that have associated units,
     * ordered by their start date in descending order.
     *
     * @return A list of active weapon Banner entities.
     */
    @Query("SELECT DISTINCT b FROM Banner b JOIN b.bannersUnits bu WHERE b.bannerType = 'weapon' AND b.isDeleted = false AND bu.isDeleted = false ORDER BY b.bannerStartDate DESC")
    List<Banner> findWeaponBanners();

    /**
     * Finds a specific character banner by its ID, ensuring it is of the correct type and not deleted.
     *
     * @param id The unique identifier of the character banner.
     * @return An Optional containing the found character Banner, or an empty Optional if not found.
     */
    @Query("SELECT b FROM Banner b JOIN b.bannersUnits bu WHERE b.id = :id AND b.bannerType = 'limited_character' AND b.isDeleted = false AND bu.isDeleted = false")
    Optional<Banner> findCharacterBannersById(@Param("id") Long id);

    /**
     * Finds a specific weapon banner by its ID, ensuring it is of the correct type and not deleted.
     *
     * @param id The unique identifier of the weapon banner.
     * @return An Optional containing the found weapon Banner, or an empty Optional if not found.
     */
    @Query("SELECT b FROM Banner b JOIN b.bannersUnits bu WHERE b.id = :id AND b.bannerType = 'weapon' AND b.isDeleted = false AND bu.isDeleted = false")
    Optional<Banner> findWeaponBannersById(@Param("id") Long id);

    /**
     * Finds any banner by its ID, provided it is not logically deleted.
     *
     * @param id The unique identifier of the banner.
     * @return An Optional containing the found Banner, or an empty Optional if not found or deleted.
     */
    @Query("SELECT b FROM Banner b WHERE b.id = :id AND b.isDeleted = false")
    Optional<Banner> findBannerById(@Param("id") Long id);

    /**
     * Performs a soft delete on a banner by setting its isDeleted flag to true.
     *
     * @param banner The Banner entity to be logically deleted.
     */
    @Modifying
    @Query("UPDATE Banner b SET b.isDeleted = true WHERE b = :banner")
    void softDeleteBanner(@Param("banner") Banner banner);
}