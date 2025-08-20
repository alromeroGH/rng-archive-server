package com.alerom.rng.archive.rng_archive_server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a join table or a relationship between a pull event and the units obtained from it.
 * This entity links Pulls and Units to define which units were received in a specific pull.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pulls_units")
public class PullsUnits {
    /**
     * The unique primary key for the pulls_units entity.
     * It is an auto-generated identity value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The pull event from which the unit was obtained.
     * This establishes a many-to-one relationship with the Pulls entity.
     */
    @ManyToOne
    @JoinColumn(name = "pull_id")
    private Pulls pull;

    /**
     * The unit obtained from the pull event.
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