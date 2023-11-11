package ru.itmo.precrimeupd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.precrimeupd.model.BossReactGroupStatistic;
import ru.itmo.precrimeupd.model.UserEntity;

public interface BossStatisticRepository extends JpaRepository<BossReactGroupStatistic, Long> {
    BossReactGroupStatistic findByBoss(UserEntity boss);
}
