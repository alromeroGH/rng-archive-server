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

@Service
public class AdminUnitService {
    @Value("${app.character-image-upload-dir:src/main/resources/images/images_characters/}")
    private String imageUploadCharacterDir;

    @Value("${app.weapon-image-upload-dir:src/main/resources/images/images_weapons/}")
    private String imageUploadWeaponDir;

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public AdminUnitService(UnitRepository unitRepository, UnitMapper unitMapper) {
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

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

    public List<UnitResponseDTO> listUnits() {
        List<Unit> units = unitRepository.getAllUnits();

        return units.stream().map(unitMapper::toResponseDTO).toList();
    }

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

        unit.setUnitImage(unitUpdateDTO.getUnitImage());

        return unitMapper.toResponseDTO(unit);
    }

    @Transactional
    public UnitResponseDTO deleteUnit(Long unitId) {
        Unit unit = unitRepository.findUnit(unitId).orElseThrow(() ->
                new UnitNotFoundException("Unit not found"));

        unitRepository.softDeleteUnit(unit);

        return unitMapper.toResponseDTO(unit);
    }
}