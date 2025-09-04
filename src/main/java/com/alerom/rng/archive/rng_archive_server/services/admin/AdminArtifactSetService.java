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

@Service
public class AdminArtifactSetService {
    @Value("${app.artifact-image-upload-dir:src/main/resources/images/images_artifacts/}")
    private String imageUploadDir;

    private final ArtifactSetRepository artifactSetRepository;
    private final ArtifactPieceRepository artifactPieceRepository;
    private final ArtifactSetMapper artifactSetMapper;
    private final ArtifactPieceMapper artifactPieceMapper;

    public AdminArtifactSetService(ArtifactSetRepository artifactSetRepository, ArtifactPieceRepository artifactPieceRepository, ArtifactSetMapper artifactSetMapper, ArtifactPieceMapper artifactPieceMapper) {
        this.artifactSetRepository = artifactSetRepository;
        this.artifactPieceRepository = artifactPieceRepository;
        this.artifactSetMapper = artifactSetMapper;
        this.artifactPieceMapper = artifactPieceMapper;
    }

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