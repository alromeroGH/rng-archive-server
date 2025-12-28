package com.alerom.rng.archive.rng_archive_server.services;

import com.alerom.rng.archive.rng_archive_server.dto.response.StatResponseDTO;
import com.alerom.rng.archive.rng_archive_server.models.Stat;
import com.alerom.rng.archive.rng_archive_server.repositories.StatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing statistic-related business logic.
 * It provides methods to retrieve available base statistics for artifacts and units.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Service
public class StatService {
    private final StatRepository statRepository;

    /**
     * Constructs the StatService with the required repository.
     *
     * @param statRepository The repository for accessing statistic data.
     */
    public StatService(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    /**
     * Retrieves a list of all active statistics registered in the system.
     * Maps the internal Stat entities to response DTOs.
     *
     * @return A list of DTOs containing statistic names and types.
     */
    public List<StatResponseDTO> listStats() {
        List<Stat> stats = statRepository.getAllStats();

        List<StatResponseDTO> statResponseDTOS = new ArrayList<>();

        for (Stat stat: stats) {
            StatResponseDTO statResponseDTO = new StatResponseDTO(
                    stat.getId(),
                    stat.getStatName(),
                    stat.getStatType()
            );

            statResponseDTOS.add(statResponseDTO);
        }

        return statResponseDTOS;
    }
}