package ru.itmo.backend.mapper;

import ru.itmo.backend.dto.PreCogOutDto;
import ru.itmo.backend.models.PreCog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PreCogMapper {
    public static PreCogOutDto mapToPreCogOutDto(PreCog preCog){
        PreCogOutDto preCogOutDto = PreCogOutDto.builder()
                .id(preCog.getId())
                .preCogName(preCog.getPreCogName())
                .age(preCog.getAge())
                .serotoninLevel(preCog.getSerotoninLevel())
                .dopamineLevel(preCog.getDopamineLevel())
                .stressLevel(preCog.getStressLevel())
                .isWork(preCog.isWork())
                .commissionedOn(convertDateToString(preCog.getCommissionedOn()))
                .build();
        return preCogOutDto;
    }

    private static String convertDateToString(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedTime = dateTime.format(formatter);
        return formattedTime;
    }
}
