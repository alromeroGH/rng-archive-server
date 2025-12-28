package com.alerom.rng.archive.rng_archive_server.services;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.ArtifactPredictionCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.ArtifactStatisticsResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.InsufficientDataException;
import com.alerom.rng.archive.rng_archive_server.exceptions.StatNotFoundException;
import com.alerom.rng.archive.rng_archive_server.models.Stat;
import com.alerom.rng.archive.rng_archive_server.models.UserArtifact;
import com.alerom.rng.archive.rng_archive_server.repositories.StatRepository;
import com.alerom.rng.archive.rng_archive_server.repositories.UserArtifactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for calculating artifact-related statistics and predictions.
 * It analyzes a user's collection to determine drop probabilities for specific main stats
 * based on historical data of sets and pieces.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Service
public class ArtifactStatisticService {

    private final UserArtifactRepository userArtifactRepository;
    private final StatRepository statRepository;

    /**
     * Constructs the ArtifactStatisticService with the necessary repositories.
     *
     * @param userArtifactRepository Repository for accessing user artifact data.
     * @param statRepository Repository for accessing statistic definitions.
     */
    public ArtifactStatisticService(UserArtifactRepository userArtifactRepository, StatRepository statRepository) {
        this.userArtifactRepository = userArtifactRepository;
        this.statRepository = statRepository;
    }

    /**
     * Calculates the probability of obtaining an artifact with a specific main stat
     * based on the user's current collection of that set and piece.
     *
     * @param artifactPredictionCreateDTO DTO containing user ID, set ID, piece ID, and the target main stat.
     * @return A DTO containing the count of related artifacts and the calculated probability percentage.
     * @throws InsufficientDataException if the user has fewer than 10 recorded artifacts for the set or piece.
     * @throws StatNotFoundException if the requested main stat ID does not exist.
     */
    public ArtifactStatisticsResponseDTO getArtifactStatistics(ArtifactPredictionCreateDTO artifactPredictionCreateDTO) {
        List<UserArtifact> setArtifacts = userArtifactRepository.findArtifactsByUserAndSet
                (artifactPredictionCreateDTO.getUserId(),
                        artifactPredictionCreateDTO.getArtifactSetId());

        List<UserArtifact> pieceArtifacts = userArtifactRepository.findArtifactsByUserAndPiece
                (artifactPredictionCreateDTO.getUserId(),
                        artifactPredictionCreateDTO.getArtifactPieceId());

        if (setArtifacts.size() < 10 || pieceArtifacts.size() < 10) {
            throw new InsufficientDataException("There is not enough data for the selected artifact");
        }

        Stat mainStatPrediction = statRepository.findStatById(artifactPredictionCreateDTO.getMainStatId()).orElseThrow(
                () -> new StatNotFoundException("Stat not found")
        );

        List<UserArtifact> mainStatSetProbability = pieceArtifacts.stream()
                .filter(p -> p.getStat().equals(mainStatPrediction))
                .toList();

        double probability = (double) mainStatSetProbability.size() / pieceArtifacts.size() * 100;

        return new ArtifactStatisticsResponseDTO(
                setArtifacts.size(),
                pieceArtifacts.size(),
                probability);
    }
}