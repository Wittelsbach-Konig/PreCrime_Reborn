package ru.itmo.backend.mapper;

import ru.itmo.backend.dto.UserInfoDto;
import ru.itmo.backend.models.UserEntity;

public class UserInfoMapper {
    public static UserInfoDto mapToUserInfo(UserEntity userEntity){
        UserInfoDto userInfo = UserInfoDto.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .login(userEntity.getLogin())
                .email(userEntity.getEmail())
                .telegramId(userEntity.getTelegramId())
                .build();
        return userInfo;
    }
}
