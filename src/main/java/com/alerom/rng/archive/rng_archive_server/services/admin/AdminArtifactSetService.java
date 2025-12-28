package com.alerom.rng.archive.rng_archive_server.services.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.ArtifactCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.ArtifactPieceUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.ArtifactUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.ArtifactResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.ArtifactNotFoundException;
import com.alerom.rng.archive.rng_archive_server.exceptions.InvalidImageException;
import com.alerom.rng.archive.rng_archive_server.exceptions.LimitException;
import com.alerom.rng.archive.rng_archive_server.mappers.ArtifactPieceMapper;
import com.alerom.rng.archive.rng_archive_server.mappers.ArtifactSetMapper;
import com.alerom.rng.archive.rng_archive_server.models.ArtifactPiece;
import com.alerom.rng.archive.rng_archive_server.models.ArtifactSet;
import com.alerom.rng.archive.rng_archive_server.repositories.ArtifactPieceRepository;
import com.alerom.rng.archive.rng_archive_server.repositories.ArtifactSetRepository;
import com.alerom.rng.archive.rng_archive_server.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for administrative management of artifact sets and pieces.
 * Handles the logic for creating complete sets, listing them with their active pieces,
 * updating set information, and performing soft deletions across the set hierarchy.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Service
public class AdminArtifactSetService {
    @Value("${app.artifact-image-upload-dir:src/main/resources/images/images_artifacts/}")
    private String imageUploadDir;

    private final ArtifactSetRepository artifactSetRepository;
    private final ArtifactPieceRepository artifactPieceRepository;
    private final ArtifactSetMapper artifactSetMapper;
    private final ArtifactPieceMapper artifactPieceMapper;

    /**
     * Constructs the AdminArtifactSetService with the required dependencies.
     *
     * @param artifactSetRepository Repository for artifact set data.
     * @param artifactPieceRepository Repository for artifact piece data.
     * @param artifactSetMapper Mapper for artifact set entity conversion.
     * @param artifactPieceMapper Mapper for artifact piece entity conversion.
     */
    public AdminArtifactSetService(ArtifactSetRepository artifactSetRepository, ArtifactPieceRepository artifactPieceRepository, ArtifactSetMapper artifactSetMapper, ArtifactPieceMapper artifactPieceMapper) {
        this.artifactSetRepository = artifactSetRepository;
        this.artifactPieceRepository = artifactPieceRepository;
        this.artifactSetMapper = artifactSetMapper;
        this.artifactPieceMapper = artifactPieceMapper;
    }

    /**
     * Creates a new artifact set along with its mandatory five pieces.
     *
     * @param artifactCreateDTO DTO containing the set information and the list of pieces to create.
     * @return A DTO representing the created set and its pieces.
     * @throws InvalidImageException if the set image fails to save.
     * @throws LimitException if the number of pieces provided is not exactly five.
     */
    @Transactional
    public ArtifactResponseDTO createArtifactSet(ArtifactCreateDTO artifactCreateDTO) {
        ArtifactSet artifactSet = artifactSetMapper
                .toEntity(artifactCreateDTO.getArtifactSet());

        String artifactImage;

        try {
            artifactImage = ImageUtils.saveBase64Image(artifactCreateDTO.getArtifactSet().getSetImage(), imageUploadDir);
        } catch (IOException e) {
            throw new InvalidImageException("Failed to save artifact set image");
        }

        artifactSet.setSetImage(artifactImage);

        artifactSet.setIsDeleted(false);

        artifactSetRepository.save(artifactSet);

        List<ArtifactPiece> artifactPieces = artifactCreateDTO.getArtifactPieces()
                .stream().map(artifactPieceMapper::toEntity)
                .toList();

        if (artifactPieces.size() != 5) {
            throw new LimitException("Amount of artifact pieces invalid");
        }

        artifactPieces.forEach(piece -> {
            piece.setArtifactSet(artifactSet);
            piece.setIsDeleted(false);
        });

        artifactPieceRepository.saveAll(artifactPieces);

        return new ArtifactResponseDTO(
                artifactSetMapper.toResponseDTO(artifactSet),
                artifactPieces.stream().map(artifactPieceMapper::toResponseDTO).toList()
        );
    }

    /**
     * Retrieves all active artifact sets and their associated active pieces.
     *
     * @return A list of DTOs containing artifact set and piece information.
     */
    public List<ArtifactResponseDTO> listArtifactSets() {
        List<ArtifactSet> artifactSets = artifactSetRepository.findAllArtifacts();

        List<ArtifactResponseDTO> artifacts = new ArrayList<>();

        for (ArtifactSet artifactSet : artifactSets) {
            List<ArtifactPiece> artifactPieces = artifactSet.getArtifactPieces()
                    .stream()
                    .filter(artifactPiece -> artifactPiece.getIsDeleted().equals(false))
                    .toList();

            artifacts.add(new ArtifactResponseDTO(
                    artifactSetMapper.toResponseDTO(artifactSet),
                    artifactPieces.stream().map(artifactPieceMapper::toResponseDTO).toList()
            ));
        }

        return artifacts;
    }

    /**
     * Updates an existing artifact set's details and its associated pieces.
     *
     * @param artifactId The unique identifier of the artifact set to update.
     * @param artifactUpdateDTO DTO containing the updated fields for the set and its pieces.
     * @return A DTO of the updated artifact set.
     * @throws ArtifactNotFoundException if the set or any specified piece ID does not exist.
     * @throws InvalidImageException if a new image is provided but fails to save.
     * @throws LimitException if the updated pieces list does not contain exactly five items.
     */
    @Transactional
    public ArtifactResponseDTO updateArtifactSet(Long artifactId, ArtifactUpdateDTO artifactUpdateDTO) {
        ArtifactSet artifactSet = artifactSetRepository.findByArtifactId(artifactId).orElseThrow(
                () -> new ArtifactNotFoundException("Artifact not found")
        );

        artifactSet.setSetName(artifactUpdateDTO.getArtifactSet().getSetName());

        String artifactImage;

        if (artifactUpdateDTO.getArtifactSet().getSetImage().startsWith("data:image")) {
            try {
                artifactImage = ImageUtils.saveBase64Image(artifactUpdateDTO.getArtifactSet().getSetImage(), imageUploadDir);
            } catch (IOException e) {
                throw new InvalidImageException("Failed to save artifact set image");
            }
        } else {
            artifactImage = artifactUpdateDTO.getArtifactSet().getSetImage();
        }

        artifactSet.setSetImage(artifactImage);

        artifactSetRepository.save(artifactSet);

        List<ArtifactPieceUpdateDTO> artifactPieceUpdateDTOS =  artifactUpdateDTO.getArtifactPieces();

        if (artifactPieceUpdateDTOS.size() != 5) {
            throw new LimitException("Amount of artifact pieces invalid, expected 5");
        }

        List<ArtifactPiece> artifactPieces = new ArrayList<>();
        for (ArtifactPieceUpdateDTO artifactPieceUpdateDTO : artifactPieceUpdateDTOS) {
            ArtifactPiece piece = artifactPieceRepository.findById(artifactPieceUpdateDTO.getId()).orElseThrow(
                    () -> new ArtifactNotFoundException("Artifact not found")
            );

            piece.setPieceName(artifactPieceUpdateDTO.getPieceName());

            artifactPieces.add(piece);
        }

        artifactPieces.forEach(piece -> {
            piece.setArtifactSet(artifactSet);
        });

        artifactPieceRepository.saveAll(artifactPieces);

        return new ArtifactResponseDTO(
                artifactSetMapper.toResponseDTO(artifactSet),
                artifactPieces.stream().map(artifactPieceMapper::toResponseDTO).toList()
        );
    }

    /**
     * Performs a soft delete on an artifact set and all its related pieces.
     *
     * @param artifactId The unique identifier of the artifact set to delete.
     * @return A DTO of the deleted artifact set and its pieces.
     * @throws ArtifactNotFoundException if the artifact set is not found.
     */
    @Transactional
    public ArtifactResponseDTO deleteArtifactSet(Long artifactId) {
        ArtifactSet artifactSet = artifactSetRepository.findByArtifactId(artifactId).orElseThrow(
                () -> new ArtifactNotFoundException("Artifact not found")
        );

        artifactSetRepository.softDelete(artifactSet);

        artifactSet.getArtifactPieces().forEach(artifactPieceRepository::softDelete);

        return new ArtifactResponseDTO(
                artifactSetMapper.toResponseDTO(artifactSet),
                artifactSet.getArtifactPieces().stream().map(artifactPieceMapper::toResponseDTO).toList()
        );
    }
}