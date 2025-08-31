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
    public CharacterBannerResponseDTO createCharacterBanner(CharacterBannerCreateDTO characterBannerCreateDTO) {
        Unit fiveStarCharacter = unitRepository.findById(characterBannerCreateDTO.getFiveStarCharacterId())
                .orElseThrow(() -> new UnitNotFoundException("Five-star character not found."));

        if (fiveStarCharacter.getNumberOfStars() != NumberOfStarsEnum.FIVE_STARS) {
            throw new InvalidUnitException("The character must be five stars");
        }

        List<Unit> fourStarCharacters = (List<Unit>) unitRepository.findAllById(characterBannerCreateDTO.getFourStarCharacterIds());

        if (fourStarCharacters.size() != 3) {
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

        BannerUnit fiveStarRelation = new BannerUnit();

        fiveStarRelation.setBanner(banner);
        fiveStarRelation.setUnit(fiveStarCharacter);
        fiveStarRelation.setIsDeleted(false);

        bannerUnitRepository.save(fiveStarRelation);

        for (Unit fourStar : fourStarCharacters) {
            if (fourStar.getNumberOfStars() != NumberOfStarsEnum.FOUR_STARS) {
                throw new InvalidUnitException("All the characters must be four stars");
            }
            BannerUnit fourStarRelation = new BannerUnit();

            fourStarRelation.setBanner(banner);
            fourStarRelation.setUnit(fourStar);
            fourStarRelation.setIsDeleted(false);

            bannerUnitRepository.save(fourStarRelation);
        }

        return new CharacterBannerResponseDTO(
                banner.getId(),
                banner.getBannerName(),
                banner.getBannerVersion(),
                banner.getBannerPhase(),
                banner.getBannerStartDate(),
                unitMapper.toResponseDTO(fiveStarCharacter),
                fourStarCharacters.stream().map(unitMapper::toResponseDTO).toList(),
                "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
        );
    }

    public List<CharacterBannerResponseDTO> listCharacterBanner() {
        List<Banner> banners = bannerRepository.findCharacterBanners();

        List<CharacterBannerResponseDTO> characterBanners = new ArrayList<>();

        for (Banner banner : banners) {
            List<BannerUnit> bannerUnits = banner.getBannersUnits().stream()
                    .filter(bannerUnit -> bannerUnit.getIsDeleted().equals(false))
                    .toList();

            Unit fiveStarCharacter = getFiveStarCharacter(bannerUnits);

            List<Unit> fourStarCharacters = getFourStarCharacters(bannerUnits);

            characterBanners.add(new CharacterBannerResponseDTO(
                    banner.getId(),
                    banner.getBannerName(),
                    banner.getBannerVersion(),
                    banner.getBannerPhase(),
                    banner.getBannerStartDate(),
                    unitMapper.toResponseDTO(fiveStarCharacter),
                    fourStarCharacters.stream().map(unitMapper::toResponseDTO).toList(),
                    "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
            ));
        }

        return characterBanners;
    }

    @Transactional
    public CharacterBannerResponseDTO updateCharacterBanner(Long bannerId, CharacterBannerUpdateDTO characterBannerUpdateDTO) {
        Banner banner = getBanner(bannerId);
        banner.getBannersUnits().forEach(c -> c.setIsDeleted(true));

        banner.setBannerName(characterBannerUpdateDTO.getBannerName());
        banner.setBannerVersion(characterBannerUpdateDTO.getBannerVersion());
        banner.setBannerPhase(characterBannerUpdateDTO.getBannerPhase());
        banner.setBannerStartDate(characterBannerUpdateDTO.getBannerStartDate());

        String bannerImageName;

        if (characterBannerUpdateDTO.getBannerImage().startsWith("data:image")) {
            try {
                bannerImageName = ImageUtils.saveBase64Image(characterBannerUpdateDTO.getBannerImage(), imageUploadDir);
            } catch (IOException e) {
                throw new InvalidImageException("Failed to save banner image");
            }
        } else {
            bannerImageName = characterBannerUpdateDTO.getBannerImage();
        }

        banner.setBannerImage(bannerImageName);

        bannerRepository.save(banner);

        Unit fiveStarCharacter = unitRepository.findById(characterBannerUpdateDTO.getFiveStarCharacterId())
                .orElseThrow(() -> new UnitNotFoundException("Five-star character not found."));

        if (fiveStarCharacter.getNumberOfStars() != NumberOfStarsEnum.FIVE_STARS) {
            throw new InvalidUnitException("The character must be five stars");
        }

        List<Unit> fourStarCharacters = (List<Unit>) unitRepository.findAllById(characterBannerUpdateDTO.getFourStarCharacterIds());

        if (fourStarCharacters.size() != 3) {
            throw new LimitException("Amount of four stars characters invalid");
        }

        BannerUnit fiveStarRelation = new BannerUnit();
        fiveStarRelation.setBanner(banner);
        fiveStarRelation.setUnit(fiveStarCharacter);
        fiveStarRelation.setIsDeleted(false);
        bannerUnitRepository.save(fiveStarRelation);

        for (Unit fourStar : fourStarCharacters) {
            if (fourStar.getNumberOfStars() != NumberOfStarsEnum.FOUR_STARS) {
                throw new InvalidUnitException("All the characters must be four stars");
            }
            BannerUnit fourStarRelation = new BannerUnit();

            fourStarRelation.setBanner(banner);
            fourStarRelation.setUnit(fourStar);
            fourStarRelation.setIsDeleted(false);

            bannerUnitRepository.save(fourStarRelation);
        }

        return new CharacterBannerResponseDTO(
                banner.getId(),
                banner.getBannerName(),
                banner.getBannerVersion(),
                banner.getBannerPhase(),
                banner.getBannerStartDate(),
                unitMapper.toResponseDTO(fiveStarCharacter),
                fourStarCharacters.stream().map(unitMapper::toResponseDTO).toList(),
                "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
        );
    }

    @Transactional
    public CharacterBannerResponseDTO deleteCharacterBanner(Long bannerId) {
        Banner banner = getBanner(bannerId);


        List<BannerUnit> bannerUnits = banner.getBannersUnits().stream()
                .filter(bannerUnit -> bannerUnit.getIsDeleted().equals(false))
                .toList();

        Unit fiveStarCharacter = getFiveStarCharacter(bannerUnits);
        List<Unit> fourStarCharacters = getFourStarCharacters(bannerUnits);

        bannerUnitRepository.softDeleteByBanner(banner);

        bannerRepository.softDeleteBanner(banner);

        return new CharacterBannerResponseDTO(
                banner.getId(),
                banner.getBannerName(),
                banner.getBannerVersion(),
                banner.getBannerPhase(),
                banner.getBannerStartDate(),
                unitMapper.toResponseDTO(fiveStarCharacter),
                fourStarCharacters.stream().map(unitMapper::toResponseDTO).toList(),
                "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
        );
    }

    private static List<Unit> getFourStarCharacters(List<BannerUnit> bannerUnits) {
        List<Unit> fourStarCharacters = bannerUnits.stream()
                .map(BannerUnit::getUnit)
                .filter(unit -> unit.getNumberOfStars().equals(NumberOfStarsEnum.FOUR_STARS))
                .toList();

        if (fourStarCharacters.size() != 3) {
            throw new LimitException("Amount of four stars characters invalid");
        } else {
            return fourStarCharacters;
        }
    }

    private static Unit getFiveStarCharacter(List<BannerUnit> bannerUnits) {
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
