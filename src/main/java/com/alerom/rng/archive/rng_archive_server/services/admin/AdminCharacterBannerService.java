package com.alerom.rng.archive.rng_archive_server.services.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.CharacterBannerCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.CharacterBannerUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.CharacterBannerResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.BannerNotFoundException;
import com.alerom.rng.archive.rng_archive_server.exceptions.InvalidUnitException;
import com.alerom.rng.archive.rng_archive_server.exceptions.LimitException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UnitNotFoundException;
import com.alerom.rng.archive.rng_archive_server.mappers.UnitMapper;
import com.alerom.rng.archive.rng_archive_server.models.Banner;
import com.alerom.rng.archive.rng_archive_server.models.BannerUnit;
import com.alerom.rng.archive.rng_archive_server.models.Unit;
import com.alerom.rng.archive.rng_archive_server.models.enums.NumberOfStarsEnum;
import com.alerom.rng.archive.rng_archive_server.repositories.BannerRepository;
import com.alerom.rng.archive.rng_archive_server.repositories.BannerUnitRepository;
import com.alerom.rng.archive.rng_archive_server.repositories.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminCharacterBannerService {
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
        Banner banner = bannerRepository.findById(characterBannerCreateDTO.getBannerId())
                .orElseThrow(() -> new BannerNotFoundException("Banner not found."));

        Unit fiveStartCharacter = unitRepository.findById(characterBannerCreateDTO.getFiveStartCharacterId())
                .orElseThrow(() -> new UnitNotFoundException("Five-star character not found."));

        if (fiveStartCharacter.getNumberOfStars() != NumberOfStarsEnum.FIVE_STARS) {
            throw new InvalidUnitException("The character must be five stars");
        }

        List<Unit> fourStartCharacters = (List<Unit>) unitRepository.findAllById(characterBannerCreateDTO.getFourStartCharacterIds());

        if (fourStartCharacters.size() != 3) {
            throw new LimitException("Amount of four stars characters invalid");
        }

        BannerUnit fiveStartRelation = new BannerUnit();

        fiveStartRelation.setBanner(banner);
        fiveStartRelation.setUnit(fiveStartCharacter);

        bannerUnitRepository.save(fiveStartRelation);

        fourStartCharacters.forEach(fs -> {
            if (fs.getNumberOfStars() != NumberOfStarsEnum.FOUR_STARS) {
                throw new InvalidUnitException("All the characters must be four stars");
            }
            BannerUnit fourStartRelation = new BannerUnit();

            fourStartRelation.setBanner(banner);
            fourStartRelation.setUnit(fs);

            bannerUnitRepository.save(fourStartRelation);
        });

        return new CharacterBannerResponseDTO(
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
        Banner banner = getBanner(characterBannerUpdateDTO.getBannerUpdateId());

        bannerUnitRepository.softDeleteByBanner(getBanner(bannerId));

        Unit fiveStartCharacter = unitRepository.findById(characterBannerUpdateDTO.getFiveStartCharacterUpdateId())
                .orElseThrow(() -> new UnitNotFoundException("Five-star character not found."));

        if (fiveStartCharacter.getNumberOfStars() != NumberOfStarsEnum.FIVE_STARS) {
            throw new InvalidUnitException("The character must be five stars");
        }

        List<Unit> fourStartCharacters = (List<Unit>) unitRepository.findAllById(characterBannerUpdateDTO.getFourStartCharacterUpdateIds());

        if (fourStartCharacters.size() != 3) {
            throw new LimitException("Amount of four stars characters invalid");
        }

        BannerUnit fiveStarRelation = new BannerUnit();
        fiveStarRelation.setBanner(banner);
        fiveStarRelation.setUnit(fiveStartCharacter);
        fiveStarRelation.setIsDeleted(false);
        bannerUnitRepository.save(fiveStarRelation);

        fourStartCharacters.forEach(fs -> {
            if (fs.getNumberOfStars() != NumberOfStarsEnum.FOUR_STARS) {
                throw new InvalidUnitException("All the characters must be four stars");
            }
            BannerUnit fourStartRelation = new BannerUnit();

            fourStartRelation.setBanner(banner);
            fourStartRelation.setUnit(fs);

            bannerUnitRepository.save(fourStartRelation);
        });

        return new CharacterBannerResponseDTO(
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