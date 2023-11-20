package ru.itmo.backend.mapper;

import ru.itmo.backend.dto.DetectiveStatisticDto;
import ru.itmo.backend.models.DetectiveStatistic;

public class DetectiveStatisticMapper {
    public static DetectiveStatisticDto mapToDetectiveStatisticDto(DetectiveStatistic detectiveStatistic){
        return DetectiveStatisticDto.builder()
                .id(detectiveStatistic.getId())
                .errorsInCards(detectiveStatistic.getErrorsInCards())
                .investigationCount(detectiveStatistic.getInvestigationCount())
                .build();
    }
}
