package com.alerom.rng.archive.rng_archive_server.services.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.UnitCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.UnitUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.UnitResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.InvalidImageException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UnitNotFoundException;
import com.alerom.rng.archive.rng_archive_server.mappers.UnitMapper;
import com.alerom.rng.archive.rng_archive_server.models.Unit;
import com.alerom.rng.archive.rng_archive_server.models.enums.UnitTypeEnum;
import com.alerom.rng.archive.rng_archive_server.repositories.UnitRepository;
import com.alerom.rng.archive.rng_archive_server.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Service class for administrative management of Units (Characters and Weapons).
 * Handles the logic for creating, listing, updating, and soft-deleting units,
 * including image persistence in type-specific directories.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Service
public class AdminUnitService {
    @Value("${app.character-image-upload-dir:src/main/resources/images/images_characters/}")
    private String imageUploadCharacterDir;

    @Value("${app.weapon-image-upload-dir:src/main/resources/images/images_weapons/}")
    private String imageUploadWeaponDir;

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    /**
     * Constructs the AdminUnitService with the required dependencies.
     *
     * @param unitRepository Repository for unit data access.
     * @param unitMapper Mapper for converting unit entities to DTOs.
     */
    public AdminUnitService(UnitRepository unitRepository, UnitMapper unitMapper) {
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

    /**
     * Creates a new unit (Character or Weapon) and saves its associated image.
     *
     * @param unitCreateDTO DTO containing unit details and Base64 image data.
     * @return A DTO of the created unit.
     * @throws InvalidImageException if the image fails to save to the filesystem.
     */
    @Transactional
    public UnitResponseDTO createUnit(UnitCreateDTO unitCreateDTO) {
        Unit unit = new Unit();

        unit.setUnitType(unitCreateDTO.getUnitType());
        unit.setUnitName(unitCreateDTO.getUnitName());
        unit.setNumberOfStars(unitCreateDTO.getNumberOfStars());
        unit.setUnitBanner(unitCreateDTO.getUnitBanner());

        String unitImage = "";

        if (unitCreateDTO.getUnitType().equals(UnitTypeEnum.WEAPON)) {
            try {
                unitImage = ImageUtils.saveBase64Image(unitCreateDTO.getUnitImage(), imageUploadWeaponDir);
            } catch (IOException e) {
                throw new InvalidImageException("Failed to save weapon image");
            }
        } else if (unitCreateDTO.getUnitType().equals(UnitTypeEnum.CHARACTER)) {
            try {
                unitImage = ImageUtils.saveBase64Image(unitCreateDTO.getUnitImage(), imageUploadCharacterDir);
            } catch (IOException e) {
                throw new InvalidImageException("Failed to save character image");
            }
        }

        unit.setUnitImage(unitImage);
        unit.setIsDeleted(false);

        unitRepository.save(unit);

        return unitMapper.toResponseDTO(unit);
    }

    /**
     * Retrieves a list of all units that are not logically deleted.
     *
     * @return A list of UnitResponseDTOs.
     */
    public List<UnitResponseDTO> listUnits() {
        List<Unit> units = unitRepository.getAllUnits();

        return units.stream().map(unitMapper::toResponseDTO).toList();
    }

    /**
     * Updates an existing unit's information, including conditional image replacement.
     *
     * @param unitId The ID of the unit to update.
     * @param unitUpdateDTO DTO containing the updated unit information.
     * @return A DTO of the updated unit.
     * @throws UnitNotFoundException if the unit with the specified ID does not exist.
     * @throws InvalidImageException if a new image is provided but fails to save.
     */
    @Transactional
    public UnitResponseDTO updateUnit(Long unitId, UnitUpdateDTO unitUpdateDTO) {
        Unit unit = unitRepository.findUnit(unitId).orElseThrow(() ->
            new UnitNotFoundException("Unit not found")
        );

        unit.setUnitType(unitUpdateDTO.getUnitType());
        unit.setUnitName(unitUpdateDTO.getUnitName());
        unit.setNumberOfStars(unitUpdateDTO.getNumberOfStars());
        unit.setUnitBanner(unitUpdateDTO.getUnitBanner());

        String unitImage = "";

        if (unitUpdateDTO.getUnitType().equals(UnitTypeEnum.WEAPON)) {
            if (unitUpdateDTO.getUnitImage().startsWith("data:image")) {
                try {
                    unitImage = ImageUtils.saveBase64Image(unitUpdateDTO.getUnitImage(), imageUploadWeaponDir);
                } catch (IOException e) {
                    throw new InvalidImageException("Failed to save weapon image");
                }
            } else {
                unitImage = unitUpdateDTO.getUnitImage();
            }
        } else if (unitUpdateDTO.getUnitType().equals(UnitTypeEnum.CHARACTER)) {
            if (unitUpdateDTO.getUnitImage().startsWith("data:image")) {
                try {
                    unitImage = ImageUtils.saveBase64Image(unitUpdateDTO.getUnitImage(), imageUploadCharacterDir);
                } catch (IOException e) {
                    throw new InvalidImageException("Failed to save character image");
                }
            } else {
                unitImage = unitUpdateDTO.getUnitImage();
            }
        }

        unit.setUnitImage(unitImage);

        return unitMapper.toResponseDTO(unit);
    }

    /**
     * Performs a soft delete on a unit by updating its status.
     *
     * @param unitId The ID of the unit to delete.
     * @return A DTO of the deleted unit.
     * @throws UnitNotFoundException if the unit does not exist.
     */
    @Transactional
    public UnitResponseDTO deleteUnit(Long unitId) {
        Unit unit = unitRepository.findUnit(unitId).orElseThrow(() ->
                new UnitNotFoundException("Unit not found"));

        unitRepository.softDeleteUnit(unit);

        return unitMapper.toResponseDTO(unit);
    }
}