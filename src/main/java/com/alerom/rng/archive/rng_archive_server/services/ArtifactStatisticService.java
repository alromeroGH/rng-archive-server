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

@Service
public class ArtifactStatisticService {

    private final UserArtifactRepository userArtifactRepository;
    private final StatRepository statRepository;

    public ArtifactStatisticService(UserArtifactRepository userArtifactRepository, StatRepository statRepository) {
        this.userArtifactRepository = userArtifactRepository;
        this.statRepository = statRepository;
    }

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