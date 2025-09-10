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

@Service
public class UserArtifactService {
    private final UserArtifactRepository userArtifactRepository;
    private final SecondaryStatRepository secondaryStatRepository;
    private final UserRepository userRepository;
    private final StatRepository statRepository;
    private final ArtifactPieceRepository artifactPieceRepository;
    private final UserArtifactMapper userArtifactMapper;

    public UserArtifactService(UserArtifactRepository userArtifactRepository, SecondaryStatRepository secondaryStatRepository, UserRepository userRepository, StatRepository statRepository, ArtifactPieceRepository artifactPieceRepository, UserArtifactMapper userArtifactMapper) {
        this.userArtifactRepository = userArtifactRepository;
        this.secondaryStatRepository = secondaryStatRepository;
        this.userRepository = userRepository;
        this.statRepository = statRepository;
        this.artifactPieceRepository = artifactPieceRepository;
        this.userArtifactMapper = userArtifactMapper;
    }

    @Transactional
    public UserArtifactResponseDTO createUserArtifact(UserArtifactCreateDTO userArtifactCreateDTO) {
        User user = userRepository.findById(userArtifactCreateDTO.getUserId()).orElseThrow(
                () -> new UserNotFoundException("Use with id " + userArtifactCreateDTO.getUserId() + " not found")
        );

        Stat mainStat = statRepository.findStatById(userArtifactCreateDTO.getMainStatId()).orElseThrow(
                () -> new StatNotFoundException("Stat with id " + userArtifactCreateDTO.getMainStatId() + " not found")
        );

        System.out.println(mainStat.getStatName());

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

    public List<UserArtifactResponseDTO> listUserArtifacts() {
        List<UserArtifact> userArtifacts = userArtifactRepository.listAllUserArtifacts();
        return userArtifacts.stream().map(userArtifactMapper::toResponseDTO).toList();
    }

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