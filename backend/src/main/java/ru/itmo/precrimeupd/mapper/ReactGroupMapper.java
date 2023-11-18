package ru.itmo.precrimeupd.mapper;

import ru.itmo.precrimeupd.dto.ReactGroupDto;
import ru.itmo.precrimeupd.model.ReactGroup;

public class ReactGroupMapper {
    public static ReactGroup mapToReactGroup(ReactGroupDto groupDto){
        ReactGroup group = ReactGroup.builder()
                .id(groupDto.getId())
                .memberName(groupDto.getMemberName())
                .telegramId(groupDto.getTelegramId())
                .build();
        return group;
    }
    public static ReactGroupDto mapToReactGroupDto(ReactGroup group){
        ReactGroupDto groupDto = new ReactGroupDto();
        groupDto.setId(group.getId());
        groupDto.setMemberName(group.getMemberName());
        groupDto.setTelegramId(group.getTelegramId());
        return groupDto;
    }
}
