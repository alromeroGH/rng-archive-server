package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.Pull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PullRepository extends JpaRepository<Pull, Long> {
    @Query("SELECT DISTINCT p FROM Pull p " +
            "JOIN FETCH p.user " +
            "JOIN FETCH p.banner " +
            "JOIN FETCH p.pullsUnits pu " +
            "JOIN FETCH pu.unit " +
            "WHERE p.isDeleted = false")
    List<Pull> findAllPulls();

    @Query("SELECT p FROM Pull p " +
            "JOIN FETCH p.user " +
            "JOIN FETCH p.banner " +
            "JOIN FETCH p.pullsUnits pu " +
            "JOIN FETCH pu.unit " +
            "WHERE p.id = :id " +
            "AND p.isDeleted = false")
    Optional<Pull> findPullById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Pull p SET p.isDeleted = true WHERE p = :pull")
    void softDeletePull(@Param("pull") Pull pull);
}