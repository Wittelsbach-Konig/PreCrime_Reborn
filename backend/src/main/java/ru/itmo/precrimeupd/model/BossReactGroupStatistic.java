package ru.itmo.precrimeupd.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "boss_react_group_statistic")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BossReactGroupStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "boss_id", nullable = false)
    private UserEntity boss;
    @Builder.Default
    private int arresstAppointed = 0;
    @Builder.Default
    private int ammoOrdered = 0;
    @Builder.Default
    private int weaponOrdered = 0;
    @Builder.Default
    private int gadgetOrdered = 0;
    @Builder.Default
    private int fuelOrdered = 0;
}
