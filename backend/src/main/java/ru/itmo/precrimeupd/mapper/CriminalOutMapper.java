package ru.itmo.precrimeupd.mapper;

import ru.itmo.precrimeupd.dto.CriminalOutDto;
import ru.itmo.precrimeupd.model.Criminal;

public class CriminalOutMapper {
    public static CriminalOutDto mapToCriminalOutDto(Criminal criminal){
        CriminalOutDto criminalOutDto = CriminalOutDto.builder()
                .id(criminal.getId())
                .name(criminal.getName())
                .weapon(criminal.getWeapon())
                .location(criminal.getLocation())
                .status(criminal.getStatus().name())
                .build();
        return criminalOutDto;
    }
}
