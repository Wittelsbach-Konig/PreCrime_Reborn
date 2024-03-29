package ru.itmo.backend.mapper;

import ru.itmo.backend.dto.CriminalOutDto;
import ru.itmo.backend.models.Criminal;

public class CriminalOutMapper {
    public static CriminalOutDto mapToCriminalOutDto(Criminal criminal){
        return CriminalOutDto.builder()
                .id(criminal.getId())
                .name(criminal.getName())
                .weapon(criminal.getWeapon())
                .location(criminal.getLocation())
                .status(criminal.getStatus().name())
                .isArrestAssigned(criminal.isArrestAssigned())
                .build();
    }
}
