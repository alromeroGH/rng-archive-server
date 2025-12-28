package com.alerom.rng.archive.rng_archive_server.controllers.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.NewsCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.NewsUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.NewsResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.NewsNotFoundException;
import com.alerom.rng.archive.rng_archive_server.services.admin.AdminNewsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for administrative management of news.
 * It provides restricted endpoints to create, list, update, and delete news articles or announcements.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/news")
public class AdminNewsController {
    private final AdminNewsService adminNewsService;

    /**
     * Constructs the AdminNewsController with the necessary service.
     * @param adminNewsService The service for administrative news operations.
     */
    public AdminNewsController(AdminNewsService adminNewsService) {
        this.adminNewsService = adminNewsService;
    }

    /**
     * Creates a new news entry.
     *
     * @param newsCreateDTO The DTO containing the details for the new news article.
     * @return A ResponseEntity containing the created news data.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createNews(@Valid @RequestBody NewsCreateDTO newsCreateDTO){
        NewsResponseDTO newsResponseDTO = adminNewsService.createNews(newsCreateDTO);

        return ResponseEntity.ok(newsResponseDTO);
    }

    /**
     * Retrieves a list of all news entries in the system.
     *
     * @return A ResponseEntity containing a list of news response DTOs.
     */
    @GetMapping
    public ResponseEntity<?> listNews() {
        List<NewsResponseDTO> newsResponseDTO = adminNewsService.listNews();

        return ResponseEntity.ok(newsResponseDTO);
    }

    /**
     * Updates an existing news entry by its unique ID.
     *
     * @param id The ID of the news to update.
     * @param newsUpdateDTO The DTO containing the updated news information.
     * @return A ResponseEntity containing the updated news data or an error message if the news entry is not found.
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateNews(@PathVariable Long id, @Valid @RequestBody NewsUpdateDTO newsUpdateDTO) {
        try {
            NewsResponseDTO newsResponseDTO = adminNewsService.updateNews(id, newsUpdateDTO);

            return ResponseEntity.ok(newsResponseDTO);
        } catch (NewsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    /**
     * Deletes a news entry from the system by its unique ID.
     *
     * @param id The ID of the news to delete.
     * @return A ResponseEntity containing the deleted news data or an error message if the news entry is not found.
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        try {
            NewsResponseDTO newsResponseDTO = adminNewsService.deleteNews(id);

            return ResponseEntity.ok(newsResponseDTO);
        } catch (NewsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}