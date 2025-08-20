package com.alerom.rng.archive.rng_archive_server.models;

import com.alerom.rng.archive.rng_archive_server.models.enums.StatTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a statistical attribute that can be found in the game, such as ATK, HP, or CRIT Rate.
 * This entity stores information about each stat and its type (main_only or both).
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stats")
public class Stats {
    /**
     * The unique primary key for the stat entity.
     * It is an auto-generated identity value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the stat (e.g., "ATK%", "CRIT Rate", "Energy Recharge").
     */
    private String statName;

    /**
     * The type of the stat, indicating where it can be only-main stat or both.
     * The value is stored as a string representation of the StatTypeEnum.
     */
    @Enumerated(EnumType.STRING)
    private StatTypeEnum statType;

    /**
     * A list of user-owned artifacts that have this stat as their main stat.
     * This defines a one-to-many relationship with the UsersArtifacts entity.
     */
    @OneToMany(mappedBy = "stat")
    private List<UsersArtifacts> usersArtifacts;

    /**
     * A list of secondary stats that have this specific stat.
     * This defines a one-to-many relationship with the SecondaryStats entity.
     */
    @OneToMany(mappedBy = "stat")
    private List<SecondaryStats> secondaryStats;

    /**
     * A boolean flag used for soft deletion, indicating if the stat is logically deleted.
     */
    private Boolean isDeleted;
}