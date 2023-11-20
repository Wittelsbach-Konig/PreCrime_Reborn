package ru.itmo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.backend.models.BossReactGroupStatistic;
import ru.itmo.backend.models.UserEntity;

public interface BossStatisticRepository extends JpaRepository<BossReactGroupStatistic, Long> {
    BossReactGroupStatistic findByBoss(UserEntity boss);
}
