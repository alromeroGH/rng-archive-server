package com.alerom.rng.archive.rng_archive_server.services.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.NewsCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.NewsUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.NewsResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.NewsNotFoundException;
import com.alerom.rng.archive.rng_archive_server.mappers.NewsMapper;
import com.alerom.rng.archive.rng_archive_server.models.News;
import com.alerom.rng.archive.rng_archive_server.repositories.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdminNewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    public AdminNewsService(NewsRepository newsRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
    }

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

    public List<NewsResponseDTO> listNews() {
        return newsRepository.getAllNews();
    }

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

    public NewsResponseDTO deleteNews(Long newsId) {
        News news = newsRepository.findNew(newsId).orElseThrow(
                () -> new NewsNotFoundException("News not found")
        );

        newsRepository.softDeleteNews(news);

        return newsMapper.toResponseDTO(news);
    }
}