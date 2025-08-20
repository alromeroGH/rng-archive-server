package com.alerom.rng.archive.rng_archive_server.models;

import com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum;
import com.alerom.rng.archive.rng_archive_server.models.enums.UnitBannerEnum;
import com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a unit in the game, which can be either a character or a weapon.
 * This entity stores information about the unit, including its name, type, and rarity.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "units")
public class Units {
    /**
     * The unique primary key for the unit entity.
     * It is an auto-generated identity value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The type of the unit (e.g., CHARACTER, WEAPON).
     * The value is stored as a string representation of the UnitTypeEnum.
     */
    @Enumerated(EnumType.STRING)
    private UnitTypeEnum unitType;

    /**
     * The unique name of the unit (e.g., "Ganyu", "Primordial Jade Cutter").
     */
    private String unitName;

    /**
     * The rarity of the unit, represented by the number of stars (e.g., 3, 4, 5).
     * The value is stored as a string representation of the NumberOfStarsEnum.
     */
    @Enumerated(EnumType.STRING)
    private NumberOfStarsEnum numberOfStars;

    /**
     * The type of banner on which the unit is available (e.g., ALL, CHARACTER, WEAPON).
     * The value is stored as a string representation of the UnitBannerEnum.
     */
    @Enumerated(EnumType.STRING)
    private UnitBannerEnum unitBanner;

    /**
     * A list of all pull events in which this unit was obtained.
     * This defines a one-to-many relationship with the PullsUnits entity.
     */
    @OneToMany(mappedBy = "unit")
    private List<PullsUnits> pullsUnits;

    /**
     * A list of all banners on which this unit has been featured.
     * This defines a one-to-many relationship with the BannersUnits entity.
     */
    @OneToMany(mappedBy = "unit")
    private List<BannersUnits> bannersUnits;

    /**
     * A URL or file path to the image representing the unit.
     */
    private String unitImage;

    /**
     * A boolean flag used for soft deletion, indicating if the unit is logically deleted.
     */
    private Boolean isDeleted;
}