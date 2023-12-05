package ru.itmo.backend.mapper;

import ru.itmo.backend.dto.ReactGroupInDto;
import ru.itmo.backend.dto.ReactGroupOutDto;
import ru.itmo.backend.models.ReactGroup;

public class ReactGroupMapper {
    public static ReactGroup mapToReactGroup(ReactGroupInDto groupDto){
        ReactGroup group = ReactGroup.builder()
                .id(groupDto.getId())
                .memberName(groupDto.getMemberName())
                .telegramId(groupDto.getTelegramId())
                .build();
        return group;
    }
    public static ReactGroupOutDto mapToReactGroupOutDto(ReactGroup group){
        ReactGroupOutDto groupDto = new ReactGroupOutDto();
        groupDto.setId(group.getId());
        groupDto.setMemberName(group.getMemberName());
        groupDto.setTelegramId(group.getTelegramId());
        groupDto.setInOperation(group.isInOperation());
        return groupDto;
    }
}
