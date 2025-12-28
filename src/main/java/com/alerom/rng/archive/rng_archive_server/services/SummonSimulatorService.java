package com.alerom.rng.archive.rng_archive_server.services;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.SummonCharacterEventCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.create.SummonWeaponEventCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.SummonCharacterEventResponseDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.SummonWeaponEventResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.BannerNotFoundException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UnitNotFoundException;
import com.alerom.rng.archive.rng_archive_server.mappers.UnitMapper;
import com.alerom.rng.archive.rng_archive_server.models.Banner;
import com.alerom.rng.archive.rng_archive_server.models.Unit;
import com.alerom.rng.archive.rng_archive_server.repositories.BannerRepository;
import com.alerom.rng.archive.rng_archive_server.repositories.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for simulating the gacha (summon) system logic.
 * It emulates probability rates, pity systems, soft pity, and specific mechanics
 * like "Capturing Radiance" and "Divine Path" for character and weapon banners.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Service
public class SummonSimulatorService {
    private final UnitRepository unitRepository;
    private final BannerRepository bannerRepository;
    private final UnitMapper unitMapper;

    /**
     * Constructs the SummonSimulatorService with the required repositories and mapper.
     *
     * @param unitRepository Repository for accessing unit data.
     * @param bannerRepository Repository for accessing banner configurations.
     * @param unitMapper Mapper to convert unit entities to response DTOs.
     */
    public SummonSimulatorService(UnitRepository unitRepository, BannerRepository bannerRepository, UnitMapper unitMapper) {
        this.unitRepository = unitRepository;
        this.bannerRepository = bannerRepository;
        this.unitMapper = unitMapper;
    }

    /**
     * Simulates a summon event for a limited character banner.
     * Implements soft pity starting at 74 pulls, the 50/50 mechanic,
     * and the Capturing Radiance system.
     *
     * @param summonCharacterEventCreateDTO DTO containing current pity status and summon amount.
     * @return A DTO containing the units obtained and the updated pity/currency state.
     * @throws BannerNotFoundException if the specified banner ID does not exist.
     * @throws UnitNotFoundException if required units (limited, standard, or 4-star) are missing from the database.
     */
    public SummonCharacterEventResponseDTO generalCharacterEventSummon(SummonCharacterEventCreateDTO summonCharacterEventCreateDTO) {
        Banner banner = bannerRepository.findBannerById(summonCharacterEventCreateDTO.getBannerId()).orElseThrow(
                () -> new BannerNotFoundException("Banner with id " + summonCharacterEventCreateDTO.getBannerId() + " not found")
        );

        // obtiene el 5 estrellas del banner
        List<Unit> fiveStarLimitedCharacters = unitRepository.getFiveStarLimitedCharacters(banner);

        if (fiveStarLimitedCharacters.isEmpty()) {
            throw new UnitNotFoundException("Five star limited characters not found");
        }

        // Guarda el presonaje de la lista en una variable ya que siempre trae solo uno
        Unit fiveStarLimitedCharacter = fiveStarLimitedCharacters.get(0);

        // obtiene los 4 estrellas del banner
        List<Unit> fourStarLimitedCharacters = unitRepository.getFourStarLimitedCharacters(banner);

        if (fourStarLimitedCharacters.isEmpty()) {
            throw new UnitNotFoundException("Four star limited characters not found");
        }

        // obtiene todos los 5 estrellas del banner permanente
        List<Unit> fiveStarStandardCharacters = unitRepository.getFiveStarStandardCharacters();

        if (fiveStarStandardCharacters.isEmpty()) {
            throw new UnitNotFoundException("Five star standard characters not found");
        }

        // Obtiene todas las unidades 4 estrellas
        List<Unit> fourStarStandardUnits = unitRepository.getFourStarStandardUnits();

        if (fourStarStandardUnits.isEmpty()) {
            throw new UnitNotFoundException("Four star standard units not found");
        }

        // Obtiene todas las armas 3 estrellas
        List<Unit> threeStarStandardWeapon = unitRepository.getThreeStarStandardWeapons();

        if (threeStarStandardWeapon.isEmpty()) {
            throw new UnitNotFoundException("Three star standard units not found");
        }

        List<Unit> units = new ArrayList<>();
        // todos los contadores que se usaran para mostrar info en el front
        // el front pasa la información actualizada
        int pityCount = summonCharacterEventCreateDTO.getPityCount();
        int primoCount = summonCharacterEventCreateDTO.getPrimoCount();
        int winFiftyFiftyCount = summonCharacterEventCreateDTO.getWinFiftyFiftyCount();
        int winCapturingRadianceCount = summonCharacterEventCreateDTO.getWinCapturingRadianceCount();

        // Verifica si perdió un 50/50
        boolean lostFiftyFifty = summonCharacterEventCreateDTO.isLostFiftyFifty();

        // Contador de 4 estrellas para obtener el asegurado
        int winFourStarCount = summonCharacterEventCreateDTO.getFourStarPityCount();

        // variables de probabilidad de obtención
        double summonProbabilityFiveStarCharacter = 0.006;
        double summonProbabilityFourStarCharacterBase = 0.051;
        double summonProbabilityWinFiftyFifty = 0.5;
        double summonProbabilityWinFourStarCharacter = 0.75;
        double summonProbabilityWinCapturingRadiance = 0.1;

        primoCount += (summonCharacterEventCreateDTO.getSummonAmount() * 160);

        for (int i = 0; i < summonCharacterEventCreateDTO.getSummonAmount(); i++) {
            pityCount++;
            winFourStarCount++;

            // al superar la tirada 74, aumenta exponencialmente
            // las pposibilidades de obtener un 5 estrellas
            if (pityCount >= 74 && pityCount < 90) {
                summonProbabilityFiveStarCharacter = 0.066 + (pityCount - 62) * 0.066;
            } else if (pityCount == 90) {
                summonProbabilityFiveStarCharacter = 1.0;
            }

            if (winFourStarCount >= 8) {
                summonProbabilityFourStarCharacterBase = 0.8;
            }

            if (Math.random() <= summonProbabilityFiveStarCharacter) {
                // obtiene 5 estrellas
                if (lostFiftyFifty || Math.random() > summonProbabilityWinFiftyFifty) {
                    // gana 50/50
                    // si perdió el 50/50 tiene asegurado el 5 estrellas
                    units.add(fiveStarLimitedCharacter);
                    pityCount = 0;
                    summonProbabilityFiveStarCharacter = 0.006;

                    if (!lostFiftyFifty) {
                        winFiftyFiftyCount++;
                    }

                    lostFiftyFifty = false;
                } else {
                    // pierde 50/50
                    if (Math.random() <= summonProbabilityWinCapturingRadiance) {
                        // gana capturing radiance, obtiene promocional
                        units.add(fiveStarLimitedCharacter);
                        lostFiftyFifty = false;
                        // aunque perdió el 50/50, ganó el capturing radiance
                        winCapturingRadianceCount++;

                        pityCount = 0;
                        summonProbabilityFiveStarCharacter = 0.006;
                    } else {
                        //pierde definitivamente el 50/50
                        units.add(fiveStarStandardCharacters.get((int) Math.floor(Math.random() * fiveStarStandardCharacters.size())));
                        lostFiftyFifty = true;

                        pityCount = 0;
                        summonProbabilityFiveStarCharacter = 0.006;
                    }
                }
            } else {
                // no obtiene el 5 estrellas
                if (Math.random() <= summonProbabilityFourStarCharacterBase) {
                    // obtiene un 4 estrellas
                    if (Math.random() < summonProbabilityWinFourStarCharacter) {
                        // obtiene uno de los 4 estrellas promocionales
                        units.add(fourStarLimitedCharacters.get((int) Math.floor(Math.random() * fourStarLimitedCharacters.size())));
                        winFourStarCount = 0;
                        summonProbabilityFourStarCharacterBase = 0.051;
                    } else {
                        // obtiene un personaje o arma 4 estrellas estandar
                        units.add(fourStarStandardUnits.get((int) Math.floor(Math.random() * fourStarStandardUnits.size())));
                        winFourStarCount = 0;
                        summonProbabilityFourStarCharacterBase = 0.051;
                    }
                } else {
                    // si el contador de 4 estrellas es mayor a 10, lo tiene garantizado
                    if (winFourStarCount >= 10) {
                        units.add(fourStarStandardUnits.get((int) Math.floor(Math.random() * fourStarStandardUnits.size())));
                        winFourStarCount = 0;
                        summonProbabilityFourStarCharacterBase = 0.051;
                    } else {
                        // obtiene un arma 3 estrellas
                        units.add(threeStarStandardWeapon.get((int) Math.floor(Math.random() * threeStarStandardWeapon.size())));
                    }
                }
            }
        }

        return new SummonCharacterEventResponseDTO(
                units.stream().map(unitMapper::toResponseDTO).toList(),
                pityCount,
                primoCount,
                winFiftyFiftyCount,
                winCapturingRadianceCount,
                lostFiftyFifty,
                winFourStarCount
        );
    }

    /**
     * Simulates a summon event for a weapon event banner.
     * Implements weapon-specific soft pity (starting at 63), the 75/25 weapon rate,
     * and the "Divine Path" (Epitomized Path) mechanic.
     *
     * @param summonWeaponEventCreateDTO DTO containing current pity, selected weapon, and divine path count.
     * @return A DTO containing the units obtained and the updated weapon banner state.
     * @throws BannerNotFoundException if the banner ID is invalid.
     * @throws UnitNotFoundException if the selected weapon or required weapon pools are not found.
     */
    public SummonWeaponEventResponseDTO generalWeaponEventSummon(SummonWeaponEventCreateDTO summonWeaponEventCreateDTO) {
        Banner banner = bannerRepository.findBannerById(summonWeaponEventCreateDTO.getBannerId()).orElseThrow(
                () -> new BannerNotFoundException("Banner with id " + summonWeaponEventCreateDTO.getBannerId() + " not found")
        );

        // obtiene el 5 estrellas del banner
        List<Unit> fiveStarLimitedWeapons = unitRepository.getFiveStarLimitedWeapons(banner);

        if (fiveStarLimitedWeapons.isEmpty()) {
            throw new UnitNotFoundException("Five star limited weapons not found");
        }

        // obtiene los 4 estrellas del banner
        List<Unit> fourStarLimitedWeapons = unitRepository.getFourStarLimitedWeapon(banner);

        if (fourStarLimitedWeapons.isEmpty()) {
            throw new UnitNotFoundException("Four star limited weapons not found");
        }

        // obtiene todos los 5 estrellas del banner permanente
        List<Unit> fiveStarStandardWeapons = unitRepository.getFiveStarStandardWeapon();

        if (fiveStarStandardWeapons.isEmpty()) {
            throw new UnitNotFoundException("Five star standard weapons not found");
        }

        // Obtiene todas las unidades 4 estrellas
        List<Unit> fourStarStandardUnits = unitRepository.getFourStarStandardUnits();

        if (fourStarStandardUnits.isEmpty()) {
            throw new UnitNotFoundException("Four star standard units not found");
        }

        // Obtiene todas las armas 3 estrellas
        List<Unit> threeStarStandardWeapon = unitRepository.getThreeStarStandardWeapons();

        List<Unit> units = new ArrayList<>();

        // todos los contadores que se usaran para mostrar info en el front
        // el front pasa la información actualizada
        int pityCount = summonWeaponEventCreateDTO.getPityCount();
        int primoCount = summonWeaponEventCreateDTO.getPrimoCount();

        // variables específicas del banner de armas
        // obtiene el estado actual de la senda divina y el arma elegida
        int divinePathCount = summonWeaponEventCreateDTO.getDivinePathCount();
        Unit weaponSelected = unitRepository.findUnit(summonWeaponEventCreateDTO.getWeaponSelected()).orElseThrow(
                () -> new UnitNotFoundException("Unit not found")
        );

        // Contador de 4 estrellas para obtener el asegurado
        int winFourStarCount = summonWeaponEventCreateDTO.getFourStarPityCount();

        // variables de probabilidad de obtención
        double summonProbabilityFiveStarWeapon = 0.007;
        double summonProbabilityWinBannerWeapon = 0.5;

        double summonProbabilityFourStarUnit = 0.06;
        double summonProbabilityWinFourStarWeapon = 0.5;

        primoCount += (summonWeaponEventCreateDTO.getSummonAmount() * 160);

        for (int i = 0; i < summonWeaponEventCreateDTO.getSummonAmount(); i++) {
            pityCount++;
            winFourStarCount++;

            // al superar la tirada 63, aumenta exponencialmente
            // las pposibilidades de obtener un 5 estrellas
            if (pityCount >= 63 && pityCount < 80) {
                summonProbabilityFiveStarWeapon = 0.007 + (pityCount - 62) * 0.074;
            } else if (pityCount == 80) {
                summonProbabilityFiveStarWeapon = 1.0;
            }

            if (winFourStarCount >= 8) {
                summonProbabilityFourStarUnit = 0.8;
            }

            if (Math.random() <= summonProbabilityFiveStarWeapon) {
                // obtiene 5 estrellas
                if (divinePathCount == 1) {
                    // si senda divina es uno tiene asegurada el arma elegida
                    // si perdió el 50/50 tiene asegurado el 5 estrellas
                    units.add(weaponSelected);
                    divinePathCount = 0;
                } else {
                    if (Math.random() <= 0.75) { // 75% para armas promocionales
                        if (Math.random() <= summonProbabilityWinBannerWeapon) {
                            units.add(weaponSelected);
                            divinePathCount = 0;
                        } else {
                            units.add(fiveStarLimitedWeapons.get(1)); // Segunda arma promocional
                            divinePathCount = 1;
                        }
                    } else {
                        units.add(fiveStarStandardWeapons.get((int) (Math.random() * fiveStarStandardWeapons.size())));
                        divinePathCount = 1;
                    }
                }
                pityCount = 0;
                summonProbabilityFiveStarWeapon = 0.007;
            } else {
                // no obtiene el arma 5 estrellas
                if (Math.random() <= summonProbabilityFourStarUnit) {
                    // obtiene un 4 estrellas
                    if (Math.random() < summonProbabilityWinFourStarWeapon) {
                        // obtiene uno de los 4 estrellas promocionales
                        units.add(fourStarLimitedWeapons.get((int) Math.floor(Math.random() * fourStarLimitedWeapons.size())));
                        winFourStarCount = 0;
                        summonProbabilityFourStarUnit = 0.06;
                    } else {
                        // obtiene un personaje o arma 4 estrellas estandar
                        units.add(fourStarStandardUnits.get((int) Math.floor(Math.random() * fourStarStandardUnits.size())));
                        winFourStarCount = 0;
                        summonProbabilityFourStarUnit = 0.06;
                    }
                } else {
                    // si el contador de 4 estrellas es mayor a 10, lo tiene garantizado
                    if (winFourStarCount == 10) {
                        units.add(fourStarStandardUnits.get((int) Math.floor(Math.random() * fourStarStandardUnits.size())));
                        winFourStarCount = 0;
                        summonProbabilityFourStarUnit = 0.06;
                    } else {
                        // obtiene un arma 3 estrellas
                        units.add(threeStarStandardWeapon.get((int) Math.floor(Math.random() * threeStarStandardWeapon.size())));
                    }
                }
            }
        }

        return new SummonWeaponEventResponseDTO(
               units.stream().map(unitMapper::toResponseDTO).toList(),
               pityCount,
               primoCount,
               divinePathCount,
               winFourStarCount
        );
    }
}