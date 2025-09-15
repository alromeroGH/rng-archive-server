package com.alerom.rng.archive.rng_archive_server.services;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.PullCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.PullUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.PullResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.BannerNotFoundException;
import com.alerom.rng.archive.rng_archive_server.exceptions.PullNotFoundException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UnitNotFoundException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UserNotFoundException;
import com.alerom.rng.archive.rng_archive_server.mappers.BannerMapper;
import com.alerom.rng.archive.rng_archive_server.mappers.PullMapper;
import com.alerom.rng.archive.rng_archive_server.mappers.UnitMapper;
import com.alerom.rng.archive.rng_archive_server.mappers.UserMapper;
import com.alerom.rng.archive.rng_archive_server.models.*;
import com.alerom.rng.archive.rng_archive_server.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PullService {
    private final PullRepository pullRepository;
    private final UserRepository userRepository;
    private final BannerRepository bannerRepository;
    private final UnitRepository unitRepository;
    private final PullUnitRepository pullUnitRepository;
    private final PullMapper pullMapper;
    private final UserMapper userMapper;
    private final BannerMapper bannerMapper;
    private final UnitMapper unitMapper;

    public PullService(PullRepository pullRepository, UserRepository userRepository, BannerRepository bannerRepository, UnitRepository unitRepository, PullUnitRepository pullUnitRepository, PullMapper pullMapper, UserMapper userMapper, BannerMapper bannerMapper, UnitMapper unitMapper) {
        this.pullRepository = pullRepository;
        this.userRepository = userRepository;
        this.bannerRepository = bannerRepository;
        this.unitRepository = unitRepository;
        this.pullUnitRepository = pullUnitRepository;
        this.pullMapper = pullMapper;
        this.userMapper = userMapper;
        this.bannerMapper = bannerMapper;
        this.unitMapper = unitMapper;
    }

    @Transactional
    public PullResponseDTO createPull(PullCreateDTO pullCreateDTO) {
        User user = userRepository.findById(pullCreateDTO.getUserId()).orElseThrow(
                () -> new UserNotFoundException("User with id " + pullCreateDTO.getUserId() + " not found")
        );

        Banner banner = bannerRepository.findBannerById(pullCreateDTO.getBannerId()).orElseThrow(
                () -> new BannerNotFoundException("Banner with id " + pullCreateDTO.getBannerId() + " not found")
        );

        Unit unit = unitRepository.findUnit(pullCreateDTO.getUnitId()).orElseThrow(
                () -> new UnitNotFoundException("Unit with id " + pullCreateDTO.getUnitId() + " not found")
        );

        Pull pull = new Pull();

        pull.setPullsAmount(pullCreateDTO.getPullsAmount());
        pull.setWon(pullCreateDTO.getWon());
        pull.setActivatedCapturingRadiance(pullCreateDTO.getActivatedCapturingRadiance());
        pull.setUser(user);
        pull.setBanner(banner);
        pull.setPullDate(new Date());
        pull.setIsDeleted(false);

        pull.addPullUnit(unit);

        pullRepository.save(pull);

        return new PullResponseDTO(
                pull.getId(),
                pull.getPullsAmount(),
                pull.getWon(),
                pull.getActivatedCapturingRadiance(),
                userMapper.toResponseDTO(user),
                bannerMapper.toResponseDTO(banner),
                unitMapper.toResponseDTO(unit)
        );
    }

    public List<PullResponseDTO> listPulls() {
        List<Pull> pulls = pullRepository.findAllPulls();

        List<PullResponseDTO> pullResponseDTOS = new ArrayList<>();

        for (Pull pull : pulls) {
            List<PullUnit> pullUnits = pull.getPullsUnits();

            Unit unit = null;
            if (pullUnits != null && !pullUnits.isEmpty()) {
                unit = pullUnits.get(0).getUnit();
            }

            PullResponseDTO pullResponseDTO = new PullResponseDTO(
                    pull.getId(),
                    pull.getPullsAmount(),
                    pull.getWon(),
                    pull.getActivatedCapturingRadiance(),
                    userMapper.toResponseDTO(pull.getUser()),
                    bannerMapper.toResponseDTO(pull.getBanner()),
                    unitMapper.toResponseDTO(unit)
            );

            pullResponseDTOS.add(pullResponseDTO);
        }

        return pullResponseDTOS;
    }

    @Transactional
    public PullResponseDTO updatePull(Long pullId, PullUpdateDTO pullUpdateDTO) {
        Pull pull = pullRepository.findPullById(pullId).orElseThrow(
                () -> new PullNotFoundException("Pull with id " + pullId + " not found")
        );

        User user = userRepository.findById(pull.getUser().getId()).orElseThrow(
                () -> new UserNotFoundException("User with id " + pull.getUser().getId() + " not found")
        );

        Banner banner = bannerRepository.findBannerById(pullUpdateDTO.getBannerId()).orElseThrow(
                () -> new BannerNotFoundException("Banner with id " + pullUpdateDTO.getBannerId() + " not found")
        );

        Unit unit = unitRepository.findUnit(pullUpdateDTO.getUnitId()).orElseThrow(
                () -> new UnitNotFoundException("Unit with id " + pullUpdateDTO.getUnitId() + " not found")
        );

        pull.setPullsAmount(pullUpdateDTO.getPullsAmount());
        pull.setWon(pullUpdateDTO.getWon());
        pull.setActivatedCapturingRadiance(pullUpdateDTO.getActivatedCapturingRadiance());
        pull.setUser(user);
        pull.setBanner(banner);

        pull.addPullUnit(unit);

        pullRepository.save(pull);

        return new PullResponseDTO(
                pull.getId(),
                pull.getPullsAmount(),
                pull.getWon(),
                pull.getActivatedCapturingRadiance(),
                userMapper.toResponseDTO(user),
                bannerMapper.toResponseDTO(banner),
                unitMapper.toResponseDTO(unit)
        );
    }

    @Transactional
    public PullResponseDTO deletePull(Long pullId) {
        Pull pull = pullRepository.findPullById(pullId).orElseThrow(
                () -> new PullNotFoundException("Pull with id " + pullId + " not found")
        );

        pullRepository.softDeletePull(pull);

        List<PullUnit> pullUnits = pull.getPullsUnits();

        Unit unit = null;
        if (pullUnits != null && !pullUnits.isEmpty()) {
            unit = pullUnits.get(0).getUnit();

            for (PullUnit pullUnit : pullUnits) {
                pullUnitRepository.softDeletePullUnit(pullUnit);
            }
        }

        return new PullResponseDTO(
                pull.getId(),
                pull.getPullsAmount(),
                pull.getWon(),
                pull.getActivatedCapturingRadiance(),
                userMapper.toResponseDTO(pull.getUser()),
                bannerMapper.toResponseDTO(pull.getBanner()),
                unitMapper.toResponseDTO(unit)
        );
    }
}