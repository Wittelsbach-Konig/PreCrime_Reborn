package ru.itmo.backend.mapper;

import ru.itmo.backend.dto.TechnicStatisticDto;
import ru.itmo.backend.models.TechnicStatistic;

public class TechnicStatisticMapper {
    public static TechnicStatisticDto mapToTechnicStatisticDto(TechnicStatistic technicStatistic){
        TechnicStatisticDto technicStatisticDto = TechnicStatisticDto.builder()
                .id(technicStatistic.getId())
                .depressantEntered(technicStatistic.getDepressantEntered())
                .dopamineEntered(technicStatistic.getDopamineEntered())
                .serotoninEntered(technicStatistic.getSerotoninEntered())
                .visionsAccepted(technicStatistic.getVisionsAccepted())
                .visionsRejected(technicStatistic.getVisionsRejected())
                .build();
        return technicStatisticDto;
    }
}
