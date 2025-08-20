package com.alerom.rng.archive.rng_archive_server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a join table or a relationship between a banner and the units it features.
 * This entity links Banners and Units to define which units are available on a specific banner.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "banners_units")
public class BannersUnits {
    /**
     * The unique primary key for the banners_units entity.
     * It is an auto-generated identity value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The banner that features the unit.
     * This establishes a many-to-one relationship with the Banners entity.
     */
    @ManyToOne
    @JoinColumn(name = "banner_id")
    private Banners banner;

    /**
     * The unit that is featured on the banner.
     * This establishes a many-to-one relationship with the Units entity.
     */
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Units unit;

    /**
     * A boolean flag used for soft deletion, indicating if the relationship is logically deleted.
     */
    private Boolean isDeleted;
}