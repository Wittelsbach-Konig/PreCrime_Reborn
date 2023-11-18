package ru.itmo.precrimeupd.mapper;

import ru.itmo.precrimeupd.dto.BossReactGroupStatisticDto;
import ru.itmo.precrimeupd.model.BossReactGroupStatistic;

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
