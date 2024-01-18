package ru.itmo.backend.mapper;

import ru.itmo.backend.dto.UserOutDto;
import ru.itmo.backend.models.UserEntity;

public class UserEntitiyMapper {
    public static UserOutDto mapToUserOutDto(UserEntity userEntity){
        return UserOutDto.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .telegramId(userEntity.getTelegramId())
                .build();
    }
}
