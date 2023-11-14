package ru.itmo.precrimeupd.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class UserStatisticInfo {
    private UserInfoDto userInfo;
    private Optional<BossReactGroupStatisticDto> bossReactGroupStatistic;
    private Optional<DetectiveStatisticDto> detectiveStatistic;
    private Optional<TechnicStatisticDto> technicStatistic;
}
