package ru.itmo.backend.mapper;

import ru.itmo.backend.dto.BossReactGroupStatisticDto;
import ru.itmo.backend.models.BossReactGroupStatistic;

public class BossStatisticMapper {
    public static BossReactGroupStatisticDto mapToBossStatisticDto(BossReactGroupStatistic bossReactGroupStatistic){
        BossReactGroupStatisticDto bossReactGroupStatisticDto = BossReactGroupStatisticDto.builder()
                .id(bossReactGroupStatistic.getId())
                .ammoOrdered(bossReactGroupStatistic.getAmmoOrdered())
                .arresstAppointed(bossReactGroupStatistic.getArresstAppointed())
                .gadgetOrdered(bossReactGroupStatistic.getGadgetOrdered())
                .fuelOrdered(bossReactGroupStatistic.getFuelOrdered())
                .weaponOrdered(bossReactGroupStatistic.getWeaponOrdered())
                .fuelSpent(bossReactGroupStatistic.getFuelSpent())
                .build();
        return bossReactGroupStatisticDto;
    }
}
