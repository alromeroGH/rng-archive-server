package com.alerom.rng.archive.rng_archive_server.services;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.UserArtifactCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.UserArtifactUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.UserArtifactResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.*;
import com.alerom.rng.archive.rng_archive_server.mappers.UserArtifactMapper;
import com.alerom.rng.archive.rng_archive_server.models.*;
import com.alerom.rng.archive.rng_archive_server.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing user artifacts business logic.
 * Handles the creation, retrieval, updating, and soft deletion of artifacts owned by users,
 * including their main and secondary statistics.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Service
public class UserArtifactService {
    private final UserArtifactRepository userArtifactRepository;
    private final SecondaryStatRepository secondaryStatRepository;
    private final UserRepository userRepository;
    private final StatRepository statRepository;
    private final ArtifactPieceRepository artifactPieceRepository;
    private final UserArtifactMapper userArtifactMapper;

    /**
     * Constructs the UserArtifactService with the required repositories and mapper.
     *
     * @param userArtifactRepository Repository for user artifact data.
     * @param secondaryStatRepository Repository for secondary statistics data.
     * @param userRepository Repository for user data.
     * @param statRepository Repository for base statistics data.
     * @param artifactPieceRepository Repository for artifact piece definitions.
     * @param userArtifactMapper Mapper to convert between entities and DTOs.
     */
    public UserArtifactService(UserArtifactRepository userArtifactRepository, SecondaryStatRepository secondaryStatRepository, UserRepository userRepository, StatRepository statRepository, ArtifactPieceRepository artifactPieceRepository, UserArtifactMapper userArtifactMapper) {
        this.userArtifactRepository = userArtifactRepository;
        this.secondaryStatRepository = secondaryStatRepository;
        this.userRepository = userRepository;
        this.statRepository = statRepository;
        this.artifactPieceRepository = artifactPieceRepository;
        this.userArtifactMapper = userArtifactMapper;
    }

    /**
     * Creates a new artifact instance for a user.
     *
     * @param userArtifactCreateDTO DTO containing owner ID, piece ID, main stat, and secondary stats.
     * @return A DTO representing the newly created user artifact.
     * @throws UserNotFoundException if the owner user does not exist.
     * @throws StatNotFoundException if the specified main stat does not exist.
     * @throws ArtifactNotFoundException if the artifact piece does not exist.
     * @throws LimitException if the number of secondary stats is not between 3 and 4.
     */
    @Transactional
    public UserArtifactResponseDTO createUserArtifact(UserArtifactCreateDTO userArtifactCreateDTO) {
        User user = userRepository.findById(userArtifactCreateDTO.getUserId()).orElseThrow(
                () -> new UserNotFoundException("Use with id " + userArtifactCreateDTO.getUserId() + " not found")
        );

        Stat mainStat = statRepository.findStatById(userArtifactCreateDTO.getMainStatId()).orElseThrow(
                () -> new StatNotFoundException("Stat with id " + userArtifactCreateDTO.getMainStatId() + " not found")
        );

        ArtifactPiece artifactPiece = artifactPieceRepository.findArtifactPieceById(userArtifactCreateDTO.getArtifactPieceId()).orElseThrow(
                () -> new ArtifactNotFoundException("Artifact piece with id " + userArtifactCreateDTO.getArtifactPieceId() + " not found")
        );

        List<Stat> stats = statRepository.findAllById(userArtifactCreateDTO.getSecondaryStatIds());

        if (stats.size() < 3 || stats.size() > 4) {
            throw new LimitException("Amount of secondary stats invalid, expected 3 or 4");
        }

        UserArtifact userArtifact = new UserArtifact();

        userArtifact.setUser(user);
        userArtifact.setStat(mainStat);
        userArtifact.setArtifactPiece(artifactPiece);
        userArtifact.setIsDeleted(false);

        for (Stat stat : stats) {
            SecondaryStat secondaryStat = new SecondaryStat();

            secondaryStat.setStat(stat);
            secondaryStat.setIsDeleted(false);

            userArtifact.addSecondaryStat(secondaryStat);
        }

        userArtifactRepository.save(userArtifact);

        return userArtifactMapper.toResponseDTO(userArtifact);
    }

    /**
     * Lists all artifacts currently stored in the system that are not deleted.
     *
     * @return A list of DTOs representing all active user artifacts.
     */
    public List<UserArtifactResponseDTO> listUserArtifacts() {
        List<UserArtifact> userArtifacts = userArtifactRepository.listAllUserArtifacts();
        return userArtifacts.stream().map(userArtifactMapper::toResponseDTO).toList();
    }

    /**
     * Updates an existing user artifact's statistics or piece information.
     * Existing secondary stats are soft-deleted and replaced by the new set.
     *
     * @param userArtifactId The ID of the artifact to update.
     * @param userArtifactUpdateDTO DTO containing the updated stats and piece info.
     * @return A DTO representing the updated artifact.
     * @throws UserArtifactNotFoundException if the user artifact does not exist.
     * @throws StatNotFoundException if the new main stat is not found.
     * @throws ArtifactNotFoundException if the new artifact piece is not found.
     * @throws LimitException if the new secondary stats count is invalid.
     */
    @Transactional
    public UserArtifactResponseDTO updateUserArtifact(Long userArtifactId, UserArtifactUpdateDTO userArtifactUpdateDTO) {
        UserArtifact userArtifact = userArtifactRepository.findUserArtifactById(userArtifactId).orElseThrow(
                () -> new UserArtifactNotFoundException("User artifact with id " + userArtifactId + " not found")
        );

        Stat mainStat = statRepository.findStatById(userArtifactUpdateDTO.getMainStatId()).orElseThrow(
                () -> new StatNotFoundException("Stat with id " + userArtifactUpdateDTO.getMainStatId() + " not found")
        );

        ArtifactPiece artifactPiece = artifactPieceRepository.findArtifactPieceById(userArtifactUpdateDTO.getArtifactPieceId()).orElseThrow(
                () -> new ArtifactNotFoundException("Artifact piece with id " + userArtifactUpdateDTO.getArtifactPieceId() + " not found")
        );

        List<SecondaryStat> secondaryStats = secondaryStatRepository.findAllById(userArtifact.getSecondaryStats().stream().map(SecondaryStat::getId).toList());

        List<Stat> stats = statRepository.findAllById(userArtifactUpdateDTO.getSecondaryStatIds());
        System.out.println("===============" + stats.size());
        if (stats.size() < 3 || stats.size() > 4) {
            throw new LimitException("Amount of secondary stats invalid, expected 3 or 4");
        }

        userArtifact.setStat(mainStat);
        userArtifact.setArtifactPiece(artifactPiece);

        for (SecondaryStat secondaryStat : secondaryStats) {
            secondaryStatRepository.softDeleteSecondaryStat(secondaryStat);
        }

        for (Stat stat : stats) {
            SecondaryStat secondaryStat = new SecondaryStat();

            secondaryStat.setStat(stat);
            secondaryStat.setIsDeleted(false);

            userArtifact.addSecondaryStat(secondaryStat);
        }

        userArtifactRepository.save(userArtifact);

        return userArtifactMapper.toResponseDTO(userArtifact);
    }

    /**
     * Performs a soft delete on a user artifact and all its associated secondary stats.
     *
     * @param userArtifactId The ID of the artifact to delete.
     * @return A DTO representing the deleted artifact.
     * @throws UserNotFoundException if the artifact is not found.
     */
    @Transactional
    public UserArtifactResponseDTO deleteUserArtifact(Long userArtifactId) {
        UserArtifact userArtifact = userArtifactRepository.findUserArtifactById(userArtifactId).orElseThrow(
                () -> new UserNotFoundException("User artifact with id " + userArtifactId + " not found")
        );

        userArtifactRepository.softDeleteUserArtifact(userArtifact);

        userArtifact.getSecondaryStats().forEach(secondaryStatRepository::softDeleteSecondaryStat);

        return userArtifactMapper.toResponseDTO(userArtifact);
    }
}