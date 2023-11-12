package ru.itmo.precrimeupd.mapper;

import ru.itmo.precrimeupd.dto.DetectiveStatisticDto;
import ru.itmo.precrimeupd.model.DetectiveStatistic;

public class DetectiveStatisticMapper {
    public static DetectiveStatisticDto mapToDetectiveStatisticDto(DetectiveStatistic detectiveStatistic){
        DetectiveStatisticDto detectiveStatisticDto = DetectiveStatisticDto.builder()
                .id(detectiveStatistic.getId())
                .errorsInCards(detectiveStatistic.getErrorsInCards())
                .investigationCount(detectiveStatistic.getInvestigationCount())
                .build();
        return detectiveStatisticDto;
    }
}
