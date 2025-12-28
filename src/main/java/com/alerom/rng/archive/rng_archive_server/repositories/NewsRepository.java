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

/**
 * Repository interface for News data.
 * Extends JpaRepository to provide standard CRUD operations for managing
 * news articles and announcements.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Retrieves all news articles that are not logically deleted, mapped to response DTOs.
     *
     * @return A list of NewsResponseDTO containing active news data.
     */
    @Query("SELECT n FROM News n WHERE n.isDeleted = false")
    List<NewsResponseDTO> getAllNews();

    /**
     * Finds a specific news article by its unique ID, provided it is not logically deleted.
     *
     * @param id The unique identifier of the news article.
     * @return An Optional containing the found News entity, or an empty Optional if not found or deleted.
     */
    @Query("SELECT n FROM News n WHERE n.id = :id AND n.isDeleted = false")
    Optional<News> findNew(@Param("id") Long id);

    /**
     * Performs a soft delete on a news article by setting its isDeleted flag to true.
     *
     * @param news The News entity to be logically deleted.
     */
    @Modifying
    @Query("UPDATE News n SET n.isDeleted = true WHERE n = :news")
    void softDeleteNews(@Param("news") News news);
}