package ru.itmo.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BossReactGroupStatisticDto {
    private Long id;
    private int arresstAppointed;
    private int ammoOrdered;
    private int weaponOrdered;
    private int gadgetOrdered;
    private int fuelOrdered;
    private int fuelSpent;
}
