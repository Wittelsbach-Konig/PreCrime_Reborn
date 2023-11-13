package ru.itmo.precrimeupd.mapper;

import ru.itmo.precrimeupd.dto.UserOutDto;
import ru.itmo.precrimeupd.model.UserEntity;

public class UserEntitiyMapper {
    public static UserOutDto mapToUserOutDto(UserEntity userEntity){
        UserOutDto userOutDto = UserOutDto.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .address(userEntity.getAddress())
                .email(userEntity.getEmail())
                .telegramId(userEntity.getTelegramId())
                .build();
        return userOutDto;
    }
}
