package com.alerom.rng.archive.rng_archive_server.services;

import com.alerom.rng.archive.rng_archive_server.dto.request.create.PullStatisticCreateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.PullStatisticResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.InsufficientDataException;
import com.alerom.rng.archive.rng_archive_server.models.Pull;
import com.alerom.rng.archive.rng_archive_server.repositories.PullRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PullStatisticService {

    private final PullRepository pullRepository;

    public PullStatisticService(PullRepository pullRepository) {
        this.pullRepository = pullRepository;
    }

    public PullStatisticResponseDTO getPullStatistics(PullStatisticCreateDTO pullStatisticCreateDTO) {
        List<Pull> userPulls = pullRepository.findUserPullsByBannerType
                (pullStatisticCreateDTO.getUserId(), pullStatisticCreateDTO.getBannerType());

        if (userPulls.isEmpty()) {
            throw new InsufficientDataException("There is not enough data for the selected banner type");
        }

        // total de tiradas
        int totalPull = 0;

        // total de unidades obtenidas
        int totalFiveUnit = userPulls.size();

        // total de unidades promocionales obtenidas
        int totalLimitedUnit = 0;

        //total de 50/50 ganados y perdidos
        int winFiftyFifty = 0;
        int lostFiftyFifty = 0;

        //total de capturing radiance ganados y perdidos
        int activateCapturingRadiance = 0;
        int notActivateCapturingRadiance = 0;

        for (Pull up : userPulls) {
            // obtiene todas las tiradas
            totalPull += up.getPullsAmount();

            // obtiene el total de unidades limitadas ganadas
            // en base a 50/50 y capturing radiance ganados
            if (up.getWon() || up.getActivatedCapturingRadiance()) {
                totalLimitedUnit++;
            }

            // obtiene todos los 50/50 ganados y perdidios
            if (up.getWon()) {
                winFiftyFifty++;
            } else {
                lostFiftyFifty++;
            }

            // obtiene todos los capturing radiance ganados y perdidios
            if (up.getActivatedCapturingRadiance()) {
                activateCapturingRadiance++;
            } else {
                notActivateCapturingRadiance++;
            }
        }

        // crea los mapas para los gráficos de torta de 50/50 y capturing radiance
        Map<String, Integer> fiftyFiftyGraphic = new HashMap<>();
        Map<String, Integer> capturingRadianceGraphic = new HashMap<>();

        fiftyFiftyGraphic.put("Win", winFiftyFifty);
        fiftyFiftyGraphic.put("Lost", lostFiftyFifty);

        capturingRadianceGraphic.put("Activate", activateCapturingRadiance);
        capturingRadianceGraphic.put("Not Activate", notActivateCapturingRadiance);

        return new PullStatisticResponseDTO(
                totalPull,
                totalFiveUnit,
                totalLimitedUnit,
                fiftyFiftyGraphic,
                capturingRadianceGraphic
        );
    }
}