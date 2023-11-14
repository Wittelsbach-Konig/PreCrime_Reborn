package ru.itmo.precrimeupd.mapper;

import ru.itmo.precrimeupd.dto.TechnicStatisticDto;
import ru.itmo.precrimeupd.model.TechnicStatistic;

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
