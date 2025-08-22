package com.alerom.rng.archive.rng_archive_server.models;

import com.alerom.rng.archive.rng_archive_server.models.enums.BannerPhaseEnum;
import com.alerom.rng.archive.rng_archive_server.models.enums.BannerTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Represents a specific banner (e.g., character or weapon event banner) in the game.
 * This entity stores information about the banner's type, version, and start date.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "banners")
public class Banner {
    /**
     * The unique primary key for the banner entity.
     * It is an auto-generated identity value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The type of banner (e.g., LIMITED_CHARACTER, WEAPON, STANDARD).
     * The value is stored as a string representation of the BannerTypeEnum.
     */
    @Enumerated(EnumType.STRING)
    private BannerTypeEnum bannerType;

    /**
     * The name of the banner (e.g., "Ballad in Goblets").
     */
    private String bannerName;

    /**
     * The version of the game in which the banner was released (e.g., "1.0", "2.1").
     */
    private String bannerVersion;

    /**
     * The phase of the banner within its version (e.g., PHASE_1, PHASE_2).
     * The value is stored as a string representation of the BannerPhaseEnum.
     */
    @Enumerated(EnumType.STRING)
    private BannerPhaseEnum bannerPhase;

    /**
     * The date on which the banner started.
     */
    private Date bannerStartDate;

    /**
     * A URL or file path to the image representing the banner.
     */
    private String bannerImage;

    /**
     * A list of all pull records made on this banner.
     * This defines a one-to-many relationship with the Pulls entity.
     */
    @OneToMany(mappedBy = "banner")
    private List<Pull> pulls;

    /**
     * A list of units (characters or weapons) featured on this banner.
     * This defines a one-to-many relationship with the BannersUnits entity.
     */
    @OneToMany(mappedBy = "banner")
    private List<BannerUnit> bannersUnits;

    /**
     * A boolean flag used for soft deletion, indicating if the banner is logically deleted.
     */
    private Boolean isDeleted;
}