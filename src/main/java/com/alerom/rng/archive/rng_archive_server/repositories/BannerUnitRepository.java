package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.Banner;
import com.alerom.rng.archive.rng_archive_server.models.BannerUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BannerUnitRepository extends JpaRepository<BannerUnit, Long> {
    @Modifying
    @Query("UPDATE BannerUnit bu SET bu.isDeleted = true WHERE bu.banner = :banner")
    void softDeleteByBanner(@Param("banner") Banner banner);
}