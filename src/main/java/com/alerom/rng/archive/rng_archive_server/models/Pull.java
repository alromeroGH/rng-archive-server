package com.alerom.rng.archive.rng_archive_server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Represents a pity pulls (gacha) event made by a user on a specific banner.
 * This entity records information about the pull, including the number of pulls,
 * the outcome of the 50/50 chance, and the date it was made.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pulls")
public class Pull {
    /**
     * The unique primary key for the pull entity.
     * It is an auto-generated identity value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The number of pulls performed to get a  5-star unit.
     * For example, 76 pulls to get Mavuika.
     */
    private int pullsAmount;

    /**
     * A boolean flag indicating whether the user won the 50/50 chance for a limited 5-star unit.
     */
    @Column(name = "won_50_50")
    private Boolean won;

    /**
     * A boolean flag indicating whether the user activated the "Capturing Radiance" mechanic.
     */
    private Boolean activatedCapturingRadiance;

    /**
     * The date and time when the pull was performed.
     */
    private Date pullDate;

    /**
     * The user who performed the pull.
     * This establishes a many-to-one relationship with the Users entity.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The banner on which the pull was performed.
     * This establishes a many-to-one relationship with the Banners entity.
     */
    @ManyToOne
    @JoinColumn(name = "banner_id")
    private Banner banner;

    /**
     * A list of units obtained in this pull event.
     * This defines a one-to-many relationship with the PullsUnits entity.
     */
    @OneToMany(mappedBy = "pull")
    private List<PullUnit> pullsUnits;

    /**
     * A boolean flag used for soft deletion, indicating if the pull record is logically deleted.
     */
    private Boolean isDeleted;
}