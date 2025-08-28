package com.alerom.rng.archive.rng_archive_server.models;

import com.alerom.rng.archive.rng_archive_server.models.enums.NewsTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Represents a news item or an event announcement in the system.
 * This entity stores information about news, including its type, content, and a link to more details.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "news")
public class News {
    /**
     * The unique primary key for the news entity.
     * It is an auto-generated identity value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The type of the news item (e.g., EVENT, BANNER, CODE).
     */
    private NewsTypeEnum newsType;

    /**
     * A brief description or summary of the news item.
     */
    private String description;

    /**
     * A URL or external link for the full details of the news item.
     */
    private String link;

    /**
     * The date on which the news item was published.
     */
    private Date dateOfPublication;

    /**
     * A boolean flag used for soft deletion, indicating if the news item is logically deleted.
     */
    private Boolean isDeleted;
}