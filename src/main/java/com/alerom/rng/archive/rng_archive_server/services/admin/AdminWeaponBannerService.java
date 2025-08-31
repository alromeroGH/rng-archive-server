package com.alerom.rng.archive.rng_archive_server.services.admin;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.WeaponBannerCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.WeaponBannerUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.WeaponBannerResponseDTO;
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
public class AdminWeaponBannerService {
    @Value("${app.banner-image-upload-dir:src/main/resources/images/images_banners/}")
    private String imageUploadDir;

    private final BannerRepository bannerRepository;
    private final BannerUnitRepository bannerUnitRepository;
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public AdminWeaponBannerService(BannerRepository bannerRepository, BannerUnitRepository bannerUnitRepository, UnitRepository unitRepository, UnitMapper unitMapper) {
        this.bannerRepository = bannerRepository;
        this.bannerUnitRepository = bannerUnitRepository;
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

    @Transactional
    public WeaponBannerResponseDTO createWeaponBanner(WeaponBannerCreateDTO weaponBannerCreateDTO) {
        List<Unit> fiveStarWeapons = (List<Unit>) unitRepository.findAllById(weaponBannerCreateDTO.getFiveStarWeaponIds());

        if (fiveStarWeapons.size() != 2) {
            throw new LimitException("Amount of five stars weapons invalid");
        }

        List<Unit> fourStarWeapons = (List<Unit>) unitRepository.findAllById(weaponBannerCreateDTO.getFourStarWeaponIds());

        if (fourStarWeapons.size() != 5) {
            throw new LimitException("Amount of four stars weapons invalid");
        }

        Banner banner = new Banner();

        banner.setBannerName(weaponBannerCreateDTO.getBannerName());
        banner.setBannerType(BannerTypeEnum.WEAPON);
        banner.setBannerVersion(weaponBannerCreateDTO.getBannerVersion());
        banner.setBannerPhase(weaponBannerCreateDTO.getBannerPhase());
        banner.setBannerStartDate(weaponBannerCreateDTO.getBannerStartDate());
        banner.setIsDeleted(false);

        String bannerImageName;
        try {
            bannerImageName = ImageUtils.saveBase64Image(weaponBannerCreateDTO.getBannerImage(), imageUploadDir);
        } catch (IOException e) {
            throw new InvalidImageException("Failed to save banner image");
        }

        banner.setBannerImage(bannerImageName);

        bannerRepository.save(banner);

        for (Unit fiveStar : fiveStarWeapons) {
            if (fiveStar.getNumberOfStars() != NumberOfStarsEnum.FIVE_STARS) {
                throw new InvalidUnitException("All the weapons must be five stars");
            }
            BannerUnit fiveStarRelation = new BannerUnit();

            fiveStarRelation.setBanner(banner);
            fiveStarRelation.setUnit(fiveStar);
            fiveStarRelation.setIsDeleted(false);

            bannerUnitRepository.save(fiveStarRelation);
        }

        for (Unit fourStar : fourStarWeapons) {
            if (fourStar.getNumberOfStars() != NumberOfStarsEnum.FOUR_STARS) {
                throw new InvalidUnitException("All the weapons must be four stars");
            }
            BannerUnit fourStarRelation = new BannerUnit();

            fourStarRelation.setBanner(banner);
            fourStarRelation.setUnit(fourStar);
            fourStarRelation.setIsDeleted(false);

            bannerUnitRepository.save(fourStarRelation);
        }

        return new WeaponBannerResponseDTO(
                banner.getId(),
                banner.getBannerName(),
                banner.getBannerVersion(),
                banner.getBannerPhase(),
                banner.getBannerStartDate(),
                fiveStarWeapons.stream().map(unitMapper::toResponseDTO).toList(),
                fourStarWeapons.stream().map(unitMapper::toResponseDTO).toList(),
                "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
        );
    }

    public List<WeaponBannerResponseDTO> listWeaponBanner() {
        List<Banner> banners = bannerRepository.findWeaponBanners();

        List<WeaponBannerResponseDTO> weaponBanners = new ArrayList<>();

        for (Banner banner : banners) {
            List<BannerUnit> bannerUnits = banner.getBannersUnits().stream()
                    .filter(bannerUnit -> bannerUnit.getIsDeleted().equals(false))
                    .toList();

            List<Unit> fiveStarWeapons = getFiveStarWeapon(bannerUnits);

            List<Unit> fourStarWeapons = getFourStarWeapons(bannerUnits);

            weaponBanners.add(new WeaponBannerResponseDTO(
                    banner.getId(),
                    banner.getBannerName(),
                    banner.getBannerVersion(),
                    banner.getBannerPhase(),
                    banner.getBannerStartDate(),
                    fiveStarWeapons.stream().map(unitMapper::toResponseDTO).toList(),
                    fourStarWeapons.stream().map(unitMapper::toResponseDTO).toList(),
                    "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
            ));
        }

        return weaponBanners;
    }

    @Transactional
    public WeaponBannerResponseDTO updateWeaponBanner(Long bannerId, WeaponBannerUpdateDTO weaponBannerUpdateDTO) {
        Banner banner = getBanner(bannerId);
        banner.getBannersUnits().forEach(c -> c.setIsDeleted(true));

        banner.setBannerName(weaponBannerUpdateDTO.getBannerName());
        banner.setBannerVersion(weaponBannerUpdateDTO.getBannerVersion());
        banner.setBannerPhase(weaponBannerUpdateDTO.getBannerPhase());
        banner.setBannerStartDate(weaponBannerUpdateDTO.getBannerStartDate());

        String bannerImageName;

        if (weaponBannerUpdateDTO.getBannerImage().startsWith("data:image")) {
            try {
                bannerImageName = ImageUtils.saveBase64Image(weaponBannerUpdateDTO.getBannerImage(), imageUploadDir);
            } catch (IOException e) {
                throw new InvalidImageException("Failed to save banner image");
            }
        } else {
            bannerImageName = weaponBannerUpdateDTO.getBannerImage();
        }

        banner.setBannerImage(bannerImageName);

        bannerRepository.save(banner);

        List<Unit> fiveStarWeapon = (List<Unit>) unitRepository.findAllById(weaponBannerUpdateDTO.getFiveStarWeaponIds());

        if (fiveStarWeapon.size() != 2) {
            throw new LimitException("Amount of five stars weapons invalid");
        }

        List<Unit> fourStarWeapons = (List<Unit>) unitRepository.findAllById(weaponBannerUpdateDTO.getFourStarWeaponIds());

        if (fourStarWeapons.size() != 5) {
            throw new LimitException("Amount of four stars weapons invalid");
        }
        for (Unit fiveStar : fiveStarWeapon) {
            if (fiveStar.getNumberOfStars() != NumberOfStarsEnum.FIVE_STARS) {
                throw new InvalidUnitException("All the weapons must be five stars");
            }
            BannerUnit fiveStarRelation = new BannerUnit();

            fiveStarRelation.setBanner(banner);
            fiveStarRelation.setUnit(fiveStar);
            fiveStarRelation.setIsDeleted(false);

            bannerUnitRepository.save(fiveStarRelation);
        }


        for (Unit fourStar : fourStarWeapons) {
            if (fourStar.getNumberOfStars() != NumberOfStarsEnum.FOUR_STARS) {
                throw new InvalidUnitException("All the weapons must be four stars");
            }
            BannerUnit fourStarRelation = new BannerUnit();

            fourStarRelation.setBanner(banner);
            fourStarRelation.setUnit(fourStar);
            fourStarRelation.setIsDeleted(false);

            bannerUnitRepository.save(fourStarRelation);
        }

        return new WeaponBannerResponseDTO(
                banner.getId(),
                banner.getBannerName(),
                banner.getBannerVersion(),
                banner.getBannerPhase(),
                banner.getBannerStartDate(),
                fiveStarWeapon.stream().map(unitMapper::toResponseDTO).toList(),
                fourStarWeapons.stream().map(unitMapper::toResponseDTO).toList(),
                "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
        );
    }

    @Transactional
    public WeaponBannerResponseDTO deleteWeaponBanner(Long bannerId) {
        Banner banner = getBanner(bannerId);

        List<BannerUnit> bannerUnits = banner.getBannersUnits().stream()
                .filter(bannerUnit -> bannerUnit.getIsDeleted().equals(false))
                .toList();

        List<Unit> fiveStarWeapon = getFiveStarWeapon(bannerUnits);
        List<Unit> fourStarWeapons = getFourStarWeapons(bannerUnits);

        bannerUnitRepository.softDeleteByBanner(banner);

        bannerRepository.softDeleteBanner(banner);

        return new WeaponBannerResponseDTO(
                banner.getId(),
                banner.getBannerName(),
                banner.getBannerVersion(),
                banner.getBannerPhase(),
                banner.getBannerStartDate(),
                fiveStarWeapon.stream().map(unitMapper::toResponseDTO).toList(),
                fourStarWeapons.stream().map(unitMapper::toResponseDTO).toList(),
                "http://localhost:8080/images/images_banners/" + banner.getBannerImage()
        );
    }

    private static List<Unit> getFourStarWeapons(List<BannerUnit> bannerUnits) {
        List<Unit> fourStarWeapons = bannerUnits.stream()
                .map(BannerUnit::getUnit)
                .filter(unit -> unit.getNumberOfStars().equals(NumberOfStarsEnum.FOUR_STARS))
                .toList();

        if (fourStarWeapons.size() != 5) {
            throw new LimitException("Amount of four stars weapons invalid");
        } else {
            return fourStarWeapons;
        }
    }

    private static List<Unit> getFiveStarWeapon(List<BannerUnit> bannerUnits) {
        List<Unit> fiveStarWeapons = bannerUnits.stream().
                map(BannerUnit::getUnit)
                .filter(unit -> unit.getNumberOfStars().equals(NumberOfStarsEnum.FIVE_STARS))
                .toList();
        fiveStarWeapons.forEach(c -> System.out.println("=======================" + c.getUnitName()));

        if (fiveStarWeapons.size() != 2) {
            throw new LimitException("Amount of five stars weapons invalid");
        } else {
            return fiveStarWeapons;
        }
    }

    private Banner getBanner(Long bannerId) {
        return bannerRepository
                .findWeaponBannersById(bannerId)
                .orElseThrow(() -> new BannerNotFoundException(
                        "Banner wit id " + bannerId + " not found"
                ));
    }
}