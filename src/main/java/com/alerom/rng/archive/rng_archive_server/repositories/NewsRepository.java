package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.dto.response.NewsResponseDTO;
import com.alerom.rng.archive.rng_archive_server.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("SELECT n FROM News n WHERE n.isDeleted = false")
    List<NewsResponseDTO> getAllNews();

    @Query("SELECT n FROM News n WHERE n.id = :id AND n.isDeleted = false")
    Optional<News> findNew(@Param("id") Long id);

    @Modifying
    @Query("UPDATE News n SET n.isDeleted = true WHERE n = :news")
    void softDeleteNews(@Param("news") News news);
}