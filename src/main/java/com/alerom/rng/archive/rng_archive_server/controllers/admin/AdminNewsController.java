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

@RestController
@RequestMapping("/api/admin/news")
public class AdminNewsController {
    private final AdminNewsService adminNewsService;

    public AdminNewsController(AdminNewsService adminNewsService) {
        this.adminNewsService = adminNewsService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNews(@Valid @RequestBody NewsCreateDTO newsCreateDTO){
        NewsResponseDTO newsResponseDTO = adminNewsService.createNews(newsCreateDTO);

        return ResponseEntity.ok(newsResponseDTO);
    }

    @GetMapping
    public ResponseEntity<?> listNews() {
        List<NewsResponseDTO> newsResponseDTO = adminNewsService.listNews();

        return ResponseEntity.ok(newsResponseDTO);
    }

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