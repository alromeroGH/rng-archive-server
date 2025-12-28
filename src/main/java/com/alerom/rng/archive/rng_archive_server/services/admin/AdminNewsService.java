package com.alerom.rng.archive.rng_archive_server.services.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.NewsCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.NewsUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.NewsResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.NewsNotFoundException;
import com.alerom.rng.archive.rng_archive_server.mappers.NewsMapper;
import com.alerom.rng.archive.rng_archive_server.models.News;
import com.alerom.rng.archive.rng_archive_server.repositories.NewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Service class for administrative management of news and announcements.
 * Handles the logic for creating, listing, updating, and soft-deleting news entries
 * used to inform users about game updates or platform changes.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Service
public class AdminNewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    /**
     * Constructs the AdminNewsService with the required dependencies.
     *
     * @param newsRepository Repository for news data access.
     * @param newsMapper Mapper for converting news entities to DTOs.
     */
    public AdminNewsService(NewsRepository newsRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
    }

    /**
     * Creates a new news entry with the current system date as the publication date.
     *
     * @param newsCreateDTO DTO containing the news type, title, description, and link.
     * @return A DTO representing the newly created news entry.
     */
    @Transactional
    public NewsResponseDTO createNews(NewsCreateDTO newsCreateDTO) {
        News news = new News();

        news.setNewsType(newsCreateDTO.getNewsType());
        news.setTitle(newsCreateDTO.getTitle());
        news.setDescription(newsCreateDTO.getDescription());
        news.setLink(newsCreateDTO.getLink());
        news.setDateOfPublication(new Date());
        news.setIsDeleted(false);

        newsRepository.save(news);

        return newsMapper.toResponseDTO(news);
    }

    /**
     * Retrieves all active news entries registered in the system.
     *
     * @return A list of NewsResponseDTOs containing all non-deleted news.
     */
    public List<NewsResponseDTO> listNews() {
        return newsRepository.getAllNews();
    }

    /**
     * Updates an existing news entry with new information.
     *
     * @param newsId The unique identifier of the news to update.
     * @param newsUpdateDTO DTO containing the updated fields for the news entry.
     * @return A DTO of the updated news.
     * @throws NewsNotFoundException if the news with the specified ID does not exist.
     */
    @Transactional
    public NewsResponseDTO updateNews (Long newsId, NewsUpdateDTO newsUpdateDTO) {
       News news = newsRepository.findNew(newsId).orElseThrow(
               () -> new NewsNotFoundException("News not found")
       );

        news.setNewsType(newsUpdateDTO.getNewsType());
        news.setTitle(newsUpdateDTO.getTitle());
        news.setDescription(newsUpdateDTO.getDescription());
        news.setLink(newsUpdateDTO.getLink());

        newsRepository.save(news);

        return newsMapper.toResponseDTO(news);
    }

    /**
     * Performs a soft delete on a specific news entry.
     *
     * @param newsId The unique identifier of the news to delete.
     * @return A DTO of the deleted news entry.
     * @throws NewsNotFoundException if the news entry is not found.
     */
    @Transactional
    public NewsResponseDTO deleteNews(Long newsId) {
        News news = newsRepository.findNew(newsId).orElseThrow(
                () -> new NewsNotFoundException("News not found")
        );

        newsRepository.softDeleteNews(news);

        return newsMapper.toResponseDTO(news);
    }
}