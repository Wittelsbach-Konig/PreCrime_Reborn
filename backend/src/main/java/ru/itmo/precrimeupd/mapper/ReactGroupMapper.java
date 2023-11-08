package ru.itmo.precrimeupd.mapper;

import ru.itmo.precrimeupd.dto.ReactGroupDto;
import ru.itmo.precrimeupd.model.ReactGroup;

public class ReactGroupMapper {
    public static ReactGroup mapToReactGroup(ReactGroupDto groupDto){
        ReactGroup group = ReactGroup.builder()
                .id(groupDto.getId())
                .driverName(groupDto.getDriverName())
                .negotiatorName(groupDto.getNegotiatorName())
                .pointManName(groupDto.getPointManName())
                .sniperName(groupDto.getSniperName())
                .telegramId(groupDto.getTelegramId())
                .build();
        return group;
    }
}
