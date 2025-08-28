package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query("SELECT DISTINCT b FROM Banner b JOIN b.bannersUnits bu WHERE b.bannerType = 'limited_character' AND b.isDeleted = false AND bu.isDeleted = false")
    List<Banner> findCharacterBanners();

    @Query("SELECT b FROM Banner b JOIN b.bannersUnits bu WHERE b.id = :id AND b.bannerType = 'limited_character' AND b.isDeleted = false AND bu.isDeleted = false")
    Optional<Banner> findCharacterBannersById(@Param("id") Long id);
}