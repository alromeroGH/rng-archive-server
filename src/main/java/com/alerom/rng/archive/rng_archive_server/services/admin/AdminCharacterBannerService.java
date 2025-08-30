package com.alerom.rng.archive.rng_archive_server.services.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.CharacterBannerCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.CharacterBannerUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.CharacterBannerResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.*;
import com.alerom.rng.archive.rng_archive_server.mappers.UnitMapper;
import com.alerom.rng.archive.rng_archive_server.models.Banner;
import com.alerom.rng.archive.rng_archive_server.models.BannerUnit;
import com.alerom.rng.archive.rng_archive_server.models.Unit;
import com.alerom.rng.archive.rng_archive_server.models.enums.BannerTypeEnum;
import com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum;
import com.alerom.rng.archive.rng_archive_server.repositories.BannerRepository;
import com.alerom.rng.archive.rng_archive_server.repositories.BannerUnitRepository;
import com.alerom.rng.archive.rng_archive_server.repositories.UnitRepository;
import com.alerom.rng.archive.rng_archive_server.utils.ImageUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminCharacterBannerService {

    @Value("${app.banner-image-upload-dir:src/main/resources/images/images_banners/}")
    private String imageUploadDir;

    private final BannerRepository bannerRepository;
    private final BannerUnitRepository bannerUnitRepository;
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public AdminCharacterBannerService(BannerRepository bannerRepository, BannerUnitRepository bannerUnitRepository, UnitRepository unitRepository, UnitMapper unitMapper) {
        this.bannerRepository = bannerRepository;
        this.bannerUnitRepository = bannerUnitRepository;
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

    @Transactional
    public CharacterBannerResponseDTO addCharacterBanner(CharacterBannerCreateDTO characterBannerCreateDTO) {
        Unit fiveStartCharacter = unitRepository.findById(characterBannerCreateDTO.getFiveStarCharacterId())
                .orElseThrow(() -> new UnitNotFoundException("Five-star character not found."));

        if (fiveStartCharacter.getNumberOfStars() != NumberOfStarsEnum.FIVE_STARS) {
            throw new InvalidUnitException("The character must be five stars");
        }

        List<Unit> fourStartCharacters = (List<Unit>) unitRepository.findAllById(characterBannerCreateDTO.getFourStarCharacterIds());

        if (fourStartCharacters.size() != 3) {
            throw new LimitException("Amount of four stars characters invalid");
        }

        Banner banner = new Banner();

        banner.setBannerName(characterBannerCreateDTO.getBannerName());
        banner.setBannerType(BannerTypeEnum.LIMITED_CHARACTER);
        banner.setBannerVersion(characterBannerCreateDTO.getBannerVersion());
        banner.setBannerPhase(characterBannerCreateDTO.getBannerPhase());
        banner.setBannerStartDate(characterBannerCreateDTO.getBannerStartDate());
        banner.setIsDeleted(false);

        String bannerImageName;
        try {
            bannerImageName = ImageUtils.saveBase64Image(characterBannerCreateDTO.getBannerImage(), imageUploadDir);
        } catch (IOException e) {
            throw new InvalidImageException("Failed to save banner image");
        }

        banner.setBannerImage(bannerImageName);

        bannerRepository.save(banner);


        BannerUnit fiveStartRelation = new BannerUnit();

        fiveStartRelation.setBanner(banner);
        fiveStartRelation.setUnit(fiveStartCharacter);
        fiveStartRelation.setIsDeleted(false);

        bannerUnitRepository.save(fiveStartRelation);

        for (Unit fs : fourStartCharacters) {
            if (fs.getNumberOfStars() != NumberOfStarsEnum.FOUR_STARS) {
                throw new InvalidUnitException("All the characters must be four stars");
            }
            BannerUnit fourStartRelation = new BannerUnit();

            fourStartRelation.setBanner(banner);
            fourStartRelation.setUnit(fs);
            fourStartRelation.setIsDeleted(false);

            bannerUnitRepository.save(fourStartRelation);
        }

        return new CharacterBannerResponseDTO(
                banner.getId(),
                banner.getBannerName(),
                banner.getBannerVersion(),
                banner.getBannerPhase(),
                banner.getBannerStartDate(),
                unitMapper.toResponseDTO(fiveStartCharacter),
                fourStartCharacters.stream().map(unitMapper::toResponseDTO).toList(),
                "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
        );
    }


    public List<CharacterBannerResponseDTO> listCharacterBanner() {
        List<Banner> banners = bannerRepository.findCharacterBanners();

        List<CharacterBannerResponseDTO> characterBanners = new ArrayList<>();

        for (Banner banner : banners) {
            List<BannerUnit> bannerUnits = banner.getBannersUnits().stream()
                    .filter(bu -> !bu.getIsDeleted())
                    .toList();

            Unit fiveStartCharacter = getFiveStartCharacter(bannerUnits);

            List<Unit> fourStartCharacters = getFourStartCharacters(bannerUnits);

            characterBanners.add(new CharacterBannerResponseDTO(
                    banner.getId(),
                    banner.getBannerName(),
                    banner.getBannerVersion(),
                    banner.getBannerPhase(),
                    banner.getBannerStartDate(),
                    unitMapper.toResponseDTO(fiveStartCharacter),
                    fourStartCharacters.stream().map(unitMapper::toResponseDTO).toList(),
                    "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
            ));
        }

        return characterBanners;
    }

    @Transactional
    public CharacterBannerResponseDTO updateCharacterBanner(Long bannerId, CharacterBannerUpdateDTO characterBannerUpdateDTO) {
        Banner banner = getBanner(bannerId);

        banner.setBannerName(characterBannerUpdateDTO.getBannerName());
        banner.setBannerVersion(characterBannerUpdateDTO.getBannerVersion());
        banner.setBannerPhase(characterBannerUpdateDTO.getBannerPhase());
        banner.setBannerStartDate(characterBannerUpdateDTO.getBannerStartDate());
        banner.setIsDeleted(false);

        String bannerImageName;
        String[] currentImageFormat = characterBannerUpdateDTO.getBannerImage().split("\\.");

        if (characterBannerUpdateDTO.getBannerImage().startsWith("data:image")) {
            try {
                bannerImageName = ImageUtils.saveBase64Image(characterBannerUpdateDTO.getBannerImage(), imageUploadDir);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save banner image", e);
            }
        } else {
            bannerImageName = characterBannerUpdateDTO.getBannerImage();
        }

        banner.setBannerImage(bannerImageName);

        bannerRepository.save(banner);

        Unit fiveStartCharacter = unitRepository.findById(characterBannerUpdateDTO.getFiveStarCharacterUpdateId())
                .orElseThrow(() -> new UnitNotFoundException("Five-star character not found."));

        if (fiveStartCharacter.getNumberOfStars() != NumberOfStarsEnum.FIVE_STARS) {
            throw new InvalidUnitException("The character must be five stars");
        }

        List<Unit> fourStartCharacters = (List<Unit>) unitRepository.findAllById(characterBannerUpdateDTO.getFourStarCharacterUpdateIds());

        if (fourStartCharacters.size() != 3) {
            throw new LimitException("Amount of four stars characters invalid");
        }

        BannerUnit fiveStarRelation = new BannerUnit();
        fiveStarRelation.setBanner(banner);
        fiveStarRelation.setUnit(fiveStartCharacter);
        fiveStarRelation.setIsDeleted(false);
        bannerUnitRepository.save(fiveStarRelation);

        for (Unit fs : fourStartCharacters) {
            if (fs.getNumberOfStars() != NumberOfStarsEnum.FOUR_STARS) {
                throw new InvalidUnitException("All the characters must be four stars");
            }
            BannerUnit fourStartRelation = new BannerUnit();

            fourStartRelation.setBanner(banner);
            fourStartRelation.setUnit(fs);
            fourStartRelation.setIsDeleted(false);

            bannerUnitRepository.save(fourStartRelation);
        }

        return new CharacterBannerResponseDTO(
                banner.getId(),
                banner.getBannerName(),
                banner.getBannerVersion(),
                banner.getBannerPhase(),
                banner.getBannerStartDate(),
                unitMapper.toResponseDTO(fiveStartCharacter),
                fourStartCharacters.stream().map(unitMapper::toResponseDTO).toList(),
                "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
        );
    }

    @Transactional
    public CharacterBannerResponseDTO deleteCharacterBanner(Long bannerId) {
        Banner banner = getBanner(bannerId);

        bannerUnitRepository.softDeleteByBanner(banner);

        List<BannerUnit> bannerUnits = banner.getBannersUnits();

        Unit fiveStartCharacter = getFiveStartCharacter(bannerUnits);
        List<Unit> fourStartCharacters = getFourStartCharacters(bannerUnits);

        return new CharacterBannerResponseDTO(
                banner.getId(),
                banner.getBannerName(),
                banner.getBannerVersion(),
                banner.getBannerPhase(),
                banner.getBannerStartDate(),
                unitMapper.toResponseDTO(fiveStartCharacter),
                fourStartCharacters.stream().map(unitMapper::toResponseDTO).toList(),
                "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
        );
    }

    private static List<Unit> getFourStartCharacters(List<BannerUnit> bannerUnits) {
        List<Unit> fourStartCharacters = bannerUnits.stream()
                .map(BannerUnit::getUnit)
                .filter(unit -> unit.getNumberOfStars().equals(NumberOfStarsEnum.FOUR_STARS))
                .toList();

        if (fourStartCharacters.size() != 3) {
            throw new LimitException("Amount of four stars characters invalid");
        } else {
            return fourStartCharacters;
        }
    }

    private static Unit getFiveStartCharacter(List<BannerUnit> bannerUnits) {
        return bannerUnits.stream().
                map(BannerUnit::getUnit)
                .filter(unit -> unit.getNumberOfStars().equals(NumberOfStarsEnum.FIVE_STARS))
                .findFirst()
                .orElse(null);
    }

    private Banner getBanner(Long bannerId) {
        return bannerRepository
                .findCharacterBannersById(bannerId)
                .orElseThrow(() -> new BannerNotFoundException(
                                "Banner wit id " + bannerId + " not found"
                ));
    }
}
