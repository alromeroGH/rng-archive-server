package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query("SELECT DISTINCT b FROM Banner b JOIN b.bannersUnits bu WHERE b.bannerType = 'limited_character' AND b.isDeleted = false AND bu.isDeleted = false ORDER BY b.bannerStartDate DESC")
    List<Banner> findCharacterBanners();

    @Query("SELECT DISTINCT b FROM Banner b JOIN b.bannersUnits bu WHERE b.bannerType = 'weapon' AND b.isDeleted = false AND bu.isDeleted = false ORDER BY b.bannerStartDate DESC")
    List<Banner> findWeaponBanners();

    @Query("SELECT b FROM Banner b JOIN b.bannersUnits bu WHERE b.id = :id AND b.bannerType = 'limited_character' AND b.isDeleted = false AND bu.isDeleted = false")
    Optional<Banner> findCharacterBannersById(@Param("id") Long id);

    @Query("SELECT b FROM Banner b JOIN b.bannersUnits bu WHERE b.id = :id AND b.bannerType = 'weapon' AND b.isDeleted = false AND bu.isDeleted = false")
    Optional<Banner> findWeaponBannersById(@Param("id") Long id);

    @Query("SELECT b FROM Banner b WHERE b.id = :id AND b.isDeleted = false")
    Optional<Banner> findBannerById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Banner b SET b.isDeleted = true WHERE b = :banner")
    void softDeleteBanner(@Param("banner") Banner banner);
}